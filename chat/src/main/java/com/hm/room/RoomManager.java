package com.hm.room;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.db.ChatMsgUtils;
import com.hm.db.GameServerManager;
import com.hm.enums.ChatRoomType;
import com.hm.model.ChatMsg;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 聊天服务器管理器
 * @date 2018年3月13日 下午2:18:44
 */
public class RoomManager {
    private static RoomManager intance = new RoomManager();

    public static RoomManager getIntance() {
        return intance;
    }

    private Map<Integer, ServerRooms> serverMap = Maps.newConcurrentMap();

    public void init() {
//        GameServerManager serverManager = GameServerManager.getInstance();
//        //启动服务器时不加载聊天记录
//        //查询出来所有大喇叭数据
//		for (String roomId : ChatMsgUtils.getRoomList()) {
//			int serverId = Integer.parseInt(roomId.split("_")[1]);
//			if (!serverManager.containServer(serverId)){
//				continue;
//			}
//			initServerRoom(roomId, serverId);
//		}
    }

    // 刷新聊天服房间
    public void refreshRooms() {
        GameServerManager.getInstance().init();
        List<Integer> serverLis = GameServerManager.getInstance().getServerList();
        Iterator<Map.Entry<Integer, ServerRooms>> it = serverMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, ServerRooms> entry = it.next();
            if (!serverLis.contains(entry.getKey())) {// 检查是否应该删掉
                it.remove();
            }
        }
        // 本聊天服新增的游戏服
        List<Integer> newServerIds = serverLis.stream().filter(e -> !serverMap.containsKey(e)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(newServerIds)) {
            for (String roomId : ChatMsgUtils.getRoomList()) {
                int serverId = Integer.parseInt(roomId.split("_")[1]);
                if (newServerIds.contains(serverId)) {
                    initServerRoom(roomId, serverId);
                }
            }
        }
    }

    private void initServerRoom(String roomId, int serverId) {
        List<ChatMsg> msgList = ChatMsgUtils.getChatMsgList(roomId);
        ServerRooms serverRooms = getServerRooms(serverId);
        //如果是区域聊天需要通过

        ChatRoom chatRoom = serverRooms.getChatRoom(roomId);
        if (chatRoom == null) {
            chatRoom = new ChatRoom(roomId);
            serverRooms.addChatRoom(chatRoom);
        }
        chatRoom.initMsgQueue(msgList);
    }

    public ServerRooms getServerRooms(int serverId) {
        return serverMap.computeIfAbsent(serverId, k -> initSystemRoom(serverId));
    }

    private ServerRooms initSystemRoom(int serverId) {
        ServerRooms serverRooms = new ServerRooms();
//        for (int i = 0; i < GameConstants.CAMP_IDS.length; i++) {
//            String systemRoomId = ChatRoomType.getSystemRoomId(serverId, GameConstants.CAMP_IDS[i]);
//            serverRooms.addChatRoom(new ChatRoom(systemRoomId));
//        }

        serverRooms.addChatRoom(new ChatRoom(StrUtil.format(GameConstants.RoomWorld, serverId)));
        serverRooms.addChatRoom(new ChatRoom(StrUtil.format(GameConstants.RoomSys, serverId)));

        return serverRooms;
    }
}
