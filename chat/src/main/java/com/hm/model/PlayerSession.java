package com.hm.model;

import cn.hutool.core.convert.Convert;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMRespCreater;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.message.MessageComm;

public abstract class PlayerSession extends DBEntity<Long> {

    public void sendErrorMsg(int code) {
        sendResponse(HMRespCreater.createSucHMResponse(MessageComm.S2C_ErrorMsg, getId(),code));
    }

    public void sendMsg(JsonMsg msg) {
        //转换msgId
        msg.setPlayerId(getId());
        sendResponse(msg.createResponse());
    }

    public void sendResponse(HMProtobuf.HMResponse response) {
        IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(getServerId());
        if(gameRpc != null) {
            gameRpc.sendPlayerHMResponse(Convert.toLong(response.getPlayerId(),0L),response.toByteArray());
        }
    }
}
