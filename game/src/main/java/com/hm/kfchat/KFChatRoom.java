package com.hm.kfchat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;
import com.hm.message.MessageComm;

import java.util.List;
import java.util.Map;

/**
 * 跨服聊天服
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 10:17
 */
public class KFChatRoom {
    private int id;
    private Map<Long, IKFPlayer> playerMap = Maps.newConcurrentMap();
    private List<KFChatMsg> msgList = Lists.newArrayList();
    private Map<Integer, Long> lastChatTime = Maps.newConcurrentMap();
    private IChatMsgDB chatMsgDB;

    public KFChatRoom(IChatMsgDB chatMsgDB) {
        this.chatMsgDB = chatMsgDB;
    }

    public void addPlayer(IKFPlayer player) {
        this.playerMap.put(player.getPlayerId(), player);
    }

    public void removePlayer(IKFPlayer player) {
        this.playerMap.remove(player.getPlayerId());
    }

    public void removePlayer(long playerId) {
        this.playerMap.remove(playerId);
    }

    public synchronized void sendMsg(KFChatMsg chatMsg) {
        if (msgList.size() > KFChatRoomManager.MaxSize) {
            try {
                this.chatMsgDB.delMsg(this.msgList.remove(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JsonMsg msg = JsonMsg.create(MessageComm.S2C_KFChatMsg);
        msg.addProperty("chatMsg", chatMsg);
        for (IKFPlayer value : playerMap.values()) {
            try {
                value.sendMsg(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.msgList.add(chatMsg);
        this.chatMsgDB.saveMsg(chatMsg);
    }

    public JsonMsg createJsonMsg() {
        JsonMsg msg = JsonMsg.create(MessageComm.S2C_KFChatMsgList);
        msg.addProperty("msgList", Lists.newArrayList(msgList));
        return msg;
    }

    public boolean isCanChatTime(long playerId) {
        if (!lastChatTime.containsKey(playerId)) {
            return true;
        }
        return System.currentTimeMillis() > lastChatTime.get(playerId) + KFChatRoomManager.ChatCheckTimeSecond * GameConstants.SECOND;
    }

    public void clearData() {
        this.msgList.clear();
        this.playerMap.clear();
        this.lastChatTime.clear();
    }

    public void initMsgFromDB(KFChatMsg chatMsg) {
        chatMsg.buildPlayerInfo();
        this.msgList.add(chatMsg);

        if (msgList.size() > KFChatRoomManager.MaxSize) {
            try {
                this.msgList.remove(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
