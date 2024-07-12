package com.hm.room;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.model.ChatMsg;
import com.hm.model.Player;

import java.util.Map;
import java.util.Queue;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 服务器当前所有聊天房间
 * @date 2018年3月13日 下午2:20:28
 */
public class ServerRooms {
    private Map<String, ChatRoom> roomMap = Maps.newConcurrentMap();

    public ServerRooms() {
    }

    public ChatRoom getChatRoom(String roomId) {
        return roomMap.get(roomId);
    }

    public void addChatRoom(ChatRoom room) {
        roomMap.put(room.getName(), room);
    }

    public void clearChatMsg() {
        roomMap.values().forEach(t -> t.clearChatMsg());
    }

    public void removeChatRoom(String roomId) {
        roomMap.remove(roomId);
    }

    public Queue<ChatMsg> addMsgList(Player player, String roomId) {
        if (StrUtil.isBlank(roomId)) {
            return null;
        }
        ChatRoom chatRoom = addRoom(player, roomId);
        return chatRoom.getMsgQueue();
    }

    public ChatRoom addRoom(Player player, String roomId) {
        ChatRoom room = roomMap.computeIfAbsent(roomId, ChatRoom::new);
        room.addChatRoom(player);
        return room;
    }

    public void clearPlayerChatMsg(long playerId) {
        roomMap.values().forEach(t -> t.clearPlayerChatMsg(playerId));
    }
}
