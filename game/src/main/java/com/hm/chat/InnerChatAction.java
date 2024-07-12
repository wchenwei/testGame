package com.hm.chat;

import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import org.springframework.stereotype.Service;

@Action
public class InnerChatAction extends InnerAction {

    @MsgMethod(ChatMessageComm.R2G_InnerMsg_Player)
    public void sendPlayerErrorMsg(JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        Player player = PlayerUtils.getOnlinePlayer(playerId);
        if(player == null) {
            return;
        }
        int msgId = msg.getInt("msgId");
        msg.setMsgId(msgId);
        if(msgId == MessageComm.S2C_ErrorMsg) {
            player.sendMsg(msgId,msg.getInt("code"));
        }else{
            player.sendMsg(msg);
        }
    }


    @MsgMethod(ChatMessageComm.S2C_BroadChatMsg)
    public void broadRoomChat(JsonMsg msg) {
        String roomId = msg.getString("roomId");
        broadMsg(roomId,msg);
    }


    public void broadMsg(String roomId, JsonMsg msg){
        int serverId = msg.getInt("serverId");
        int roomType = msg.getInt("roomType");
        ChatRoomType chatRoomType = ChatRoomType.getType(roomType);
        switch (chatRoomType){
            case World:
            case System:
                PlayerContainer.broadPlayer(serverId,msg);
                return;
            case Guild:
                int guildId =  Integer.parseInt(roomId.split("_")[2]);
                PlayerContainer.broadPlayerByGuild(guildId,msg);
                return;

        }
    }


}
