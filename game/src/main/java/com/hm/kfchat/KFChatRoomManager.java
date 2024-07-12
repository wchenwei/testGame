package com.hm.kfchat;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.enums.ActionType;
import com.hm.enums.KfType;
import com.hm.log.LogBiz;
import com.hm.redis.PlayerRedisData;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 跨服聊天服管理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 10:17
 */
public class KFChatRoomManager {
    private static KFChatRoomManager instance = new KFChatRoomManager();

    public static KFChatRoomManager getInstance() {
        return instance;
    }

    public Map<Integer, KFChatRoom> roomMap = Maps.newHashMap();
    //每个服务器id的组信息
    public Map<Integer, Integer> serverGroupMap = Maps.newHashMap();
    @Getter
    public KfType kfType;


    public static long closeChatSTime;
    public static long closeChatETime;
    public static long ChatCheckTimeSecond = 3;//聊天间隔秒数
    public static int MaxSize = 100;//最大保留聊天数

    @Getter
    private IChatMsgDB chatMsgDB;

    public void init(IChatMsgDB chatMsgDB) {
        this.kfType = chatMsgDB.getKFType();
        this.chatMsgDB = chatMsgDB;
        System.out.println("跨服聊天加载成功:" + kfType.getDesc());

        List<KFChatMsg> chatMsgs = chatMsgDB.queryAll();
        for (KFChatMsg chatMsg : chatMsgs) {
            KFChatRoom room = this.roomMap.get(chatMsg.getGid());
            if (room == null) {
                room = new KFChatRoom(this.chatMsgDB);
                this.roomMap.put(chatMsg.getGid(), room);
            }
            room.initMsgFromDB(chatMsg);
        }
    }

    public void createChatRoom(int roomId) {
        this.roomMap.put(roomId, new KFChatRoom(this.chatMsgDB));
    }

    public void removeChatRoom(int roomId) {
        KFChatRoom room = this.roomMap.remove(roomId);
        if (room != null) {
            room.clearData();
        }
    }

    public void clearAllRoom() {
        try {
            this.serverGroupMap.clear();
            for (KFChatRoom value : roomMap.values()) {
                value.clearData();
            }
            roomMap.clear();
            KFTeamGroup.deleteType(this.kfType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KFChatRoom getChatRoom(int groupId) {
        return this.roomMap.get(groupId);
    }

    public KFChatRoom getChatRoom(IKFPlayer player) {
        return getChatRoom(player.getGroupId());
    }


    public synchronized void addPlayer2Room(IKFPlayer player, PlayerRedisData redisData) {
        KFChatRoom room = getChatRoom(player.getGroupId());
        if (room == null) {
            room = new KFChatRoom(this.chatMsgDB);
            this.roomMap.put(player.getGroupId(), room);
        }
        room.addPlayer(player);
        //发送跨服聊天
        player.sendMsg(room.createJsonMsg());

        savePlayerTeamForGroup(player);
        LogBiz logBiz = SpringUtil.getBean(LogBiz.class);
        logBiz.addPlayerActionLog(redisData, ActionType.KFJoin, String.valueOf(this.getKfType().getType()));
    }

    public void removePlayer2Room(IKFPlayer player) {
        KFChatRoom room = getChatRoom(player.getGroupId());
        if (room != null) {
            room.removePlayer(player);
        }
    }

    public void savePlayerTeamForGroup(IKFPlayer player) {
        if (this.serverGroupMap.containsKey(player.getTeamId())) {
            return;
        }
        this.serverGroupMap.put(player.getTeamId(), player.getGroupId());
        new KFTeamGroup(player).saveDB();
    }


    public static boolean isCloseChat() {
        if (closeChatSTime <= 0 && closeChatETime <= 0) {
            return false;
        }
        long now = System.currentTimeMillis();
        return now >= closeChatSTime && now <= closeChatSTime;
    }
}
