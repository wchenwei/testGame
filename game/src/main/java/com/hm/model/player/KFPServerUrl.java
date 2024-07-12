package com.hm.model.player;

import com.hm.libcore.rpc.IGameRpc;
import com.hm.rpc.GameRpcManager;
import lombok.Data;

@Data
public class KFPServerUrl {
    private int serverId;
    private String url;
    private int type;

    public KFPServerUrl(int serverId, String url, int type) {
        this.serverId = serverId;
        this.url = url;
        this.type = type;
    }


    public IGameRpc getGameRpc() {
        return GameRpcManager.getAndStartRpc(url);
    }
}
