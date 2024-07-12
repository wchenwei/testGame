package com.hm.rpc;

import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.soketserver.ISession;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RpcSession implements ISession {
    private String serverUrl;
    private long playerId;

    public RpcSession(String serverUrl, long playerId) {
        this.serverUrl = serverUrl;
        this.playerId = playerId;
    }

    @Override
    public boolean isConnect() {
        return true;
    }

    @Override
    public void clearSession() {

    }

    @Override
    public void write(HMProtobuf.HMResponse response) {
        GameRpcManager.sendPlayerHMResponse(playerId,serverUrl,response);
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }
}
