package com.hm.action;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.ChatServerConfig;
import com.hm.SignUtils;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.container.FilterContainer;
import com.hm.container.PlayerContainer;
import com.hm.db.ChatMsgUtils;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.ChatRoomType;
import com.hm.enums.CommonValueType;
import com.hm.http.HttpBiz;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.GameIdUtils;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.ChatMsg;
import com.hm.model.Player;
import com.hm.redis.GuildRedisData;
import com.hm.redis.RedisUtil;
import com.hm.room.ChatRoom;
import com.hm.room.KfRoomManager;
import com.hm.room.RoomManager;
import com.hm.room.ServerRooms;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Biz
public class ChatBiz {
    @Resource
    private PlayerContainer playerContainer;
    @Resource
    private FilterContainer filterContainer;
    @Resource
    private HttpBiz httpBiz;
    @Resource
    private CommValueConfig commValueConfig;

    public void doLoginOut(Player player) {
        playerContainer.removePlayer(player.getId());
        this.leaveServer(player);
    }

    public void addPlayer(Player player) {
        JsonMsg serverMsg = new JsonMsg(ChatMessageComm.S2C_SendHistoryMsg);
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(player.getServerId());
        for (ChatRoomType roomType : ChatRoomType.PlayerRoomList) {
            String roomId = roomType.getChatRoomId(player.getServerId(), player);
            if (roomId == null) {
                continue;
            }
            log.debug(String.format("玩家[%s]加入房间[%s]", player.getName(), roomId));
            Queue<ChatMsg> msgQueue = rooms.addMsgList(player, roomId);
            serverMsg.addProperty(String.valueOf(roomType.getType()), msgQueue);
        }
        player.sendMsg(serverMsg);
    }

    /**
     * 发消息
     */
    public void sendMsg(Player player, String content, ChatRoomType roomType, ChatMsgType chatMsgType, JsonMsg msg) {
        long now = System.currentTimeMillis();
        if (!checkChatByConfig(player, roomType, now)) {
            System.out.println(player.getId()+" 聊天cd");
            return;//判断时间间隔
        }
        long playerId = player.getId();
        if (filterContainer.isNoChat(playerId)) {
            System.out.println(player.getId()+" 禁言");
            return;
        }
        //系统过滤字过滤
        boolean isCheck = isCheckChat(roomType, chatMsgType, playerId);
//        if (isCheck && !checkSign(msg, playerId)) {
//            filterContainer.blackHouse(player);
//            return;
//        }
//        //设置玩家过滤次数计数并返回是否需要禁言
//        boolean isBackHome = isCheck && filterContainer.doWorldPlayerNow(player, StringUtil.replaceBlank(msg.getString("content")), content, roomType);
//        if (isBackHome) {
//            return;
//        }

        if (isCheck) {
            content = StrUtil.subPre(content, 300);
            content = SysWordSensitiveUtil.getInstance().replaceSensitiveWord(content, "*");
        }
        String extend = msg.getString("extend"); // 扩展字段,红包代表红包id
        sendChatMsg(player, player.getServerId(), content, chatMsgType, roomType, extend);
        player.addChatTimeRecord(now);
    }

    private boolean checkChatByConfig(Player player, ChatRoomType roomType, long now) {
        //判断上次发言间隔
//        int diff = commValueConfig.getIntValue(CommonValueType.Chat_CD);

        if (now - player.getLastChatTime() < 1000) {
            return false;
        }
        return true;
    }

    /**
     * 把游戏服的消息转发到聊天室
     */
    public void sendMsgFromInner(long playerId, int serverId, int campId,
                                 int areaId, String content, int msgType, int roomType, String extend) {
        Player player = playerId > 0 ? playerContainer.getPlayer(playerId) : null;
        if (player != null) {
            player.initPlayerData();
        }
        ChatMsgType chatMsgType = ChatMsgType.getType(msgType);
        Set<Pair<String, ChatRoomType>> roomIds = Sets.newHashSet();
        ChatRoomType chatRoomType = ChatRoomType.getType(roomType);

        switch (chatRoomType) {
            case System: // 系统频道本阵营
                roomIds.add(new Pair<>(ChatRoomType.getSystemRoomId(serverId), ChatRoomType.System));
                break;
            case Guild: // 系统频道本阵营
                roomIds.add(new Pair<>(ChatRoomType.Guild.getChatRoomId(serverId,player), ChatRoomType.Guild));
                break;
            case World: // 系统频道本阵营
                roomIds.add(new Pair<>(StrUtil.format(GameConstants.RoomWorld, serverId), ChatRoomType.World));
                break;
        }
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        ChatMsg chatMsg = null;
        for (Pair<String, ChatRoomType> roomId : roomIds) {
            ChatRoom chatRoom = rooms.getChatRoom(roomId.getKey());
            if (chatRoom == null) {
                continue;
            }
            chatMsg = createChatMsg(playerId, content, chatMsgType, roomId.getValue(), chatRoom, extend, serverId);
            chatRoom.chat(player, roomId.getValue(), chatMsg);
        }
        //大喇叭单独保存
        if (chatMsgType == ChatMsgType.Horn && chatMsg != null) {
            ChatMsgUtils.save(chatMsg, GameConstants.HornName);
        }
    }

    /**
     * 发后台消息
     */
    public void sendGMMsg(int serverId, long playerId, String content) {
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        sendChatMsg(player, serverId, content, ChatMsgType.Notice, ChatRoomType.System, null);
    }

    public void sendChatMsg(Player player, int serverId, String content, ChatMsgType chatMsgType, ChatRoomType roomType, String extend) {
        ChatRoom chatRoom = getChatRoom(player, serverId, roomType);
        if (chatRoom != null) {
            long playerId = player == null ? 0 : player.getId();
            ChatMsg chatMsg = createChatMsg(playerId, content, chatMsgType, roomType, chatRoom, extend, serverId);
            chatRoom.chat(player, roomType, chatMsg);
        }else{
            System.out.println("找不到房间:"+player.getId()+" serverId:"+serverId+" roomType:"+roomType);
        }
    }

    public static ChatRoom getChatRoom(Player player, int serverId, ChatRoomType roomType) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        String chatRoomId = roomType.getChatRoomId(serverId, player);
        if(chatRoomId == null) {
            return null;
        }
        ChatRoom chatRoom = rooms.getChatRoom(chatRoomId);
        if(chatRoom == null) {
            chatRoom = new ChatRoom(chatRoomId);
            rooms.addChatRoom(chatRoom);
        }
        return chatRoom;
    }

    private ChatMsg createChatMsg(long playerId, String content, ChatMsgType chatMsgType, ChatRoomType roomType, ChatRoom chatRoom, String extend, int serverId) {
        ChatMsg chatMsg = new ChatMsg(playerId, content, chatMsgType, roomType, extend, serverId);
        String id = GameIdUtils.nextStrId();
        chatMsg.setId(roomType.buildId(id));
        return chatMsg;
    }

    public void quitGuild(int serverId, long playerId, int guildId) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        String guildRoomId = ChatRoomType.getGuildRoomId(serverId, guildId);
        ChatRoom room = rooms.getChatRoom(guildRoomId);
        if (room != null) {
            room.leaveRoom(player);
        }
    }

    public void joinGuild(int serverId, long playerId, int guildId) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        player.setGuildId(guildId);
        GuildRedisData guildRedisData = RedisUtil.getGuildRedisData(guildId);
        if (guildRedisData != null) {
            player.setGuildInfo(guildRedisData);
        }
        JsonMsg serverMsg = new JsonMsg(ChatMessageComm.S2C_SendHistoryMsg);
        serverMsg.addProperty(String.valueOf(ChatRoomType.Guild.getType()), rooms.addMsgList(player, player.getGuildRoomId()));
        player.sendMsg(serverMsg);
    }

    /**
     * 加入阵营
     */
    public void joinCamp(int serverId, long playerId, int oldCampId, int newCampId) {
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        player.setCamp(newCampId);

        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        JsonMsg serverMsg = new JsonMsg(ChatMessageComm.S2C_SendHistoryMsg);
        Queue<ChatMsg> msgQueue = rooms.addMsgList(player, player.getCampRoomId());
        serverMsg.addProperty(String.valueOf(ChatRoomType.Camp.getType()), msgQueue);
        player.sendMsg(serverMsg);

        // 同步切换区域频道
        if (player.getAreaId() > 0) { // 创号选完阵营后，主城还未创建，此时区域为0
            joinArea(player, rooms);
            leaveArea(serverId, oldCampId, player.getAreaId(), player, rooms);
        }

        // 离开旧阵营房间
        leaveCamp(serverId, oldCampId, player, rooms);
    }

    private void leaveCamp(int serverId, int campId, Player player, ServerRooms rooms) {
        if (campId <= 0) {
            return;
        }

        String roomId = ChatRoomType.getCampRoomId(serverId, campId);
        ChatRoom room = rooms.getChatRoom(roomId);
        if (room != null) {
            room.leaveRoom(player);
        }
    }

    public void quitCamp(int serverId, long playerId, int camp) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        leaveCamp(serverId, camp, player, rooms);
    }

    public void delGuildRoom(int serverId, int guildId) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        String guildRoomId = ChatRoomType.getGuildRoomId(serverId, guildId);
        rooms.removeChatRoom(guildRoomId);
    }

    public void delPlayerChat(int serverId, long playerId) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        rooms.clearPlayerChatMsg(playerId);
    }

    public void sendKfMsg(long playerId, String roomId, String content) {
        //系统过滤字过滤
        String filterContent = SysWordSensitiveUtil.getInstance().replaceSensitiveWord(content, "*");
        Player player = playerContainer.getPlayer(playerId);
        if (player != null) {
            sendKfChatMsg(player, roomId, filterContent);
        }
    }

    private void sendKfChatMsg(Player player, String roomId, String content) {
        ChatRoom chatRoom = KfRoomManager.getIntance().getChatRoom(roomId);
        if (chatRoom != null) {
//			ChatMsg chatMsg = createChatMsg(playerId, content,chatRoom);
//			chatRoom.chat(chatMsg);
            new ChatMsg(player.getId(), content, player.getServerId());
        }
    }


    private boolean checkSign(JsonMsg msg, long playerId) {
        Map<String, String> paramMap = Maps.newConcurrentMap();
        String content = msg.getString("content");
        String time = msg.getString("time");
        String now = msg.getString("now");
        String sign = msg.getString("sign");
        paramMap.put("content", content);
        paramMap.put("time", time); // 输入聊天内容花费的时间
        paramMap.put("now", now); // 客户端的系统时间
        boolean checkSign = StrUtil.equals(sign, SignUtils.buildSing(paramMap, ChatServerConfig.getInstance().getChatcheckkey()));
        if (!checkSign) {
            log.info(playerId + "--sign校验未通过,关小黑屋");
            return false;
        }
        boolean checkInputLegal = this.checkInputLegal(content, Convert.toLong(time), Convert.toLong(now));
        if (!checkInputLegal) {
            log.info(playerId + "--输入校验未通过，关小黑屋--" + content + ",time:" + time + ",now:" + now);
            return false;
        }
        return true;
    }

    /**
     * 检验输入合法性
     */
    private boolean checkInputLegal(String content, long time, long now) {
        //校验时间合法性
        log.info(StrUtil.format("now->{}, 现在->{}", now, System.currentTimeMillis()));
        if(time<=0||(System.currentTimeMillis()-now> 5* GameConstants.MINUTE)){
            log.error("时间校验出错-----"+time);
            return false;
        }
        //检验输入合法性
        double useTimeAvg = NumberUtil.div(content.getBytes().length,time/100);
        boolean flag = useTimeAvg<1.3;
        if(!flag){
            log.error("输入时长校验未通过，用时---"+time);
            return false;
        }
        return true;
    }

    private boolean isCheckChat(ChatRoomType roomType, ChatMsgType chatMsgType, long playerId) {
        return ChatRoomType.System != roomType && chatMsgType == ChatMsgType.Normal && playerId > 0;
    }

    private void leaveServer(Player player) {
        ServerRooms rooms = RoomManager.getIntance().getServerRooms(player.getServerId());
        Arrays.stream(ChatRoomType.values())
                .map(roomType -> roomType.getChatRoomId(player.getServerId(), player))
                .filter(Objects::nonNull)
                .map(rooms::getChatRoom)
                .filter(Objects::nonNull).forEach(chatRoom -> chatRoom.leaveRoom(player));
    }

    /**
     * 玩家区域变化
     */
    public void changeArea(int serverId, long playerId, int campId, int oldAreaId, int newAreaId) {
        Player player = playerContainer.getPlayer(playerId);
        if (player == null) {
            return;
        }
        player.setAreaId(newAreaId);

        ServerRooms rooms = RoomManager.getIntance().getServerRooms(serverId);
        joinArea(player, rooms);
        // 离开旧区域房间
        leaveArea(serverId, campId, oldAreaId, player, rooms);
    }

    private void joinArea(Player player, ServerRooms rooms) {
        JsonMsg serverMsg = new JsonMsg(ChatMessageComm.S2C_SendHistoryMsg);
        Queue<ChatMsg> msgQueue = rooms.addMsgList(player, player.getAreaRoomId());
        serverMsg.addProperty(String.valueOf(ChatRoomType.Area.getType()), msgQueue);
        player.sendMsg(serverMsg);
    }

    private void leaveArea(int serverId, int campId, int areaId, Player player, ServerRooms rooms) {
        if (campId <= 0 || areaId <= 0) {
            return;
        }
        String roomId = ChatRoomType.getAreaRoomId(serverId, campId, areaId);
        ChatRoom room = rooms.getChatRoom(roomId);
        if (room != null) {
            room.leaveRoom(player);
        }
    }
}
