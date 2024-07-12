package com.hm.action;

import com.hm.message.ChatMessageComm;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.ConfigLoad;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.ChatRoomType;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.message.MessageComm;

import javax.annotation.Resource;
import java.util.List;

@Action
public class GameInnerAction extends InnerAction {
    @Resource
    private ChatBiz chatBiz;

    @MsgMethod(MessageComm.G2C_SyncRpcServer)
    public void syncServer(JsonMsg msg) {
        List<Integer> serverIdList = (List<Integer>)msg.getObject("serverIds");
        String rpcUrl = msg.getString("rpcUrl");
        for (int serverId : serverIdList) {
            GameRpcClientUtils.changeServerRpc(serverId,rpcUrl);
        }
    }

    @MsgMethod(ChatMessageComm.G2C_SendChatMsg)
    public void sendChatMsg(JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        int serverId = msg.getInt("serverId");
        int campId = msg.getInt("campId");
        int areaId = msg.getInt("areaId");
        String content = msg.getString("content");
        int msgType = msg.getInt("msgType");
        int roomType = msg.getInt("roomType");
        String extend = msg.getString("extend"); // 扩展字段,红包代表红包id
        chatBiz.sendMsgFromInner(playerId, serverId, campId, areaId, content, msgType, roomType, extend);
    }

    @MsgMethod(ChatMessageComm.G2C_JoinGuild)
    public void joinGuild(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        int guildId = msg.getInt("guildId");
        chatBiz.joinGuild(serverId, playerId, guildId);
    }

    @MsgMethod(ChatMessageComm.G2C_QuitGuild)
    public void quitGuild(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        int guildId = msg.getInt("guildId");
        chatBiz.quitGuild(serverId, playerId, guildId);
    }

    @MsgMethod(ChatMessageComm.G2C_JoinCamp)
    public void joinCamp(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        int oldCampId = msg.getInt("oldCampId");
        int newCampId = msg.getInt("newCampId");
        chatBiz.joinCamp(serverId, playerId, oldCampId, newCampId);
    }

    @MsgMethod(ChatMessageComm.G2C_QuitCamp)
    public void quitCamp(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        int camp = msg.getInt("camp");
        chatBiz.quitCamp(serverId, playerId, camp);
    }


    @MsgMethod(ChatMessageComm.G2C_DelGuild)
    public void delGuild(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        int guildId = msg.getInt("guildId");
        chatBiz.delGuildRoom(serverId, guildId);
    }

    @MsgMethod(ChatMessageComm.G2C_GagPlayer)
    public void gagPlayer(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        chatBiz.delPlayerChat(serverId, playerId);
    }

    @MsgMethod(ChatMessageComm.G2C_SysRedMsg)
    public void sendSysRedMsg(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        String extend = msg.getString("extend"); //扩展字段,红包代表红包id
        int camp = msg.getInt("camp");
        String content = msg.getString("content");
        ChatRoomType roomType = ChatRoomType.getType(msg.getInt("roomType"));
        chatBiz.sendChatMsg(null, serverId, content, ChatMsgType.RedPacket, roomType, extend);
    }

    @MsgMethod(ChatMessageComm.G2C_ClearChat)
    public void clearChat(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        int chatMsgType = msg.getInt("chatMsgType");
    }

    @MsgMethod(ChatMessageComm.G2C_ChangeArea)
    public void changeArea(JsonMsg msg) {
        int serverId = msg.getInt("serverId");
        long playerId = msg.getInt("playerId");
        int camp = msg.getInt("camp");
        int oldAreaId = msg.getInt("oldAreaId");
        int newAreaId = msg.getInt("newAreaId");
        chatBiz.changeArea(serverId, playerId, camp, oldAreaId, newAreaId);
    }

    @MsgMethod(ChatMessageComm.G2C_ReloadJson)
    public void reloadJson(JsonMsg msg) {
        ConfigLoad.loadAllConfig();
    }
}
