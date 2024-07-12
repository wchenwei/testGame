package com.hm.room;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author xjt
 * @version V1.0
 * @Description: 聊天服务器跨服聊天管理器
 * @date 2021年1月19日11:57:08
 */
public class KfRoomManager {
    private static KfRoomManager intance = new KfRoomManager();

    public static KfRoomManager getIntance() {
        return intance;
    }

    private Map<String, ChatRoom> roomMap = Maps.newConcurrentMap();

    public ChatRoom getChatRoom(String roomId) {
        ChatRoom room = roomMap.get(roomId);
        if (room == null) {
            room = new ChatRoom(roomId);
            roomMap.put(roomId, room);
        }
        return room;
    }


}
