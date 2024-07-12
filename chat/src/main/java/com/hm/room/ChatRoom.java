package com.hm.room;

import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.db.ChatMsgUtils;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.ChatRoomType;
import com.hm.model.ChatMsg;
import com.hm.model.Player;
import com.hm.libcore.rpc.GameRpcClientUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ChatRoom extends Broadcaster {
    private String roomId;
    private Map<Long, Player> playerMap = Maps.newConcurrentMap();
    private Queue<ChatMsg> msgQueue = new ConcurrentLinkedQueue<ChatMsg>(); //按时间正序排列
    private ReentrantLock roomLock = new ReentrantLock();

    public ChatRoom(String roomId) {
        super(roomId);
        this.roomId = roomId;
    }

    public void leaveRoom(Player player) {
        Player oldPlayer = playerMap.remove(player.getId());
        if (oldPlayer != null) {
            leaveGroup(oldPlayer);
            log.debug(String.format("玩家[%s]离开房间[%s]", player.getName(), getName()));
        }
    }

    public void addChatRoom(Player player) {
        Player oldPlayer = playerMap.get(player.getId());
        if (oldPlayer != null) {
            leaveRoom(oldPlayer);
        }
        playerMap.put(player.getId(), player);
        addGroup(player);
    }

    public void chat(Player player, ChatRoomType roomType, ChatMsg msg) {
        try {
            roomLock.lock();
            if (player != null) {
                msg.initPlayerInfo(player);
            }
            //只保存文字和语音聊天内容
            if (ChatRoomType.isSave(msg.getRoomType()) && ChatMsgType.isSave(msg.getMsgType())) {
                if (msg.getRoomType() == ChatRoomType.Area.getType()) {
                    msg.setRoomId(getName());
                }
                ChatMsgUtils.save(msg, msg.getDBCollName(msg.getServerId(), roomType));
            }
            addChatMsg(msg);
            //广播给当前房间里的所有人
            JsonMsg chatMsg = new JsonMsg(ChatMessageComm.S2C_BroadChatMsg);
            chatMsg.addProperty("msg", msg);
            chatMsg.addProperty("roomId", roomId);
            chatMsg.addProperty("roomType", roomType.getType());
            if(player != null) {
                chatMsg.addProperty("playerId", player.getId());
                chatMsg.addProperty("serverId", player.getServerId());
            }else{
                chatMsg.addProperty("playerId", msg.getPlayerId());
                chatMsg.addProperty("serverId", msg.getServerId());
            }
            int serverId = player != null?player.getServerId():msg.getServerId();
            broadcastMessageForRoom(serverId,chatMsg);
        } finally {
            roomLock.unlock();
        }
    }

    public void broadcastMessageForRoom(int serverId,JsonMsg msg) {
        try {
            IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(serverId);
            if(gameRpc != null) {
                gameRpc.sendInnerMsg(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addChatMsg(ChatMsg msg) {
        msgQueue.offer(msg); //加入队尾
        if(msgQueue.size() > GameConstants.ChatMaxSize){
            msgQueue.poll(); //删除对头
        }
//        if (msgQueue.size() <= chatLimit + hornLimit) {
//            return;
//        }
//        if (hornCount > hornLimit) {
//            ChatMsg pollMsg = msgQueue.poll();//删除队头
//            if (pollMsg != null && pollMsg.getMsgType() == ChatMsgType.Horn.getType()) {
//                hornCount--;
//            }
//            return;
//        }
//
//        if (msg.getMsgType() == ChatMsgType.Horn.getType()) {
//            hornCount++;
//        }
//        // 喇叭未满时，把最早的非喇叭记录删除
//        Iterator<ChatMsg> it = msgQueue.iterator();
//        while (it.hasNext()) {
//            ChatMsg chat = it.next();
//            if (chat.getMsgType() != ChatMsgType.Horn.getType()) {
//                it.remove();
//                break;
//            }
//        }
    }

    public void clearChatMsg() {
        msgQueue.clear();
    }

    public void initMsgQueue(List<ChatMsg> msgList) {
        for (ChatMsg chatMsg : msgList) {
            chatMsg.initPlayerInfo();
            addChatMsg(chatMsg);
        }
    }

    public Queue<ChatMsg> getMsgQueue() {
        return msgQueue;
    }

    public void clearPlayerChatMsg(long playerId) {
        msgQueue.removeIf(m -> m != null && m.getPlayerId() == playerId);
    }
}
