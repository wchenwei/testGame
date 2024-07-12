package com.hm.rpc;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.rpc.IRPC;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.message.MessageComm;
import com.hm.server.GameServerManager;
import com.hm.util.GameIpUtils;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class RpcManager {
    public Map<String, IRPC> rpcMap = Maps.newConcurrentMap();

    //发送rpc消息
    public void sendMsg(KFRpcType kfRpcType,String url, JsonMsg jsonMsg) {
        try {
            IRPC rpc = getAndStartRpc(kfRpcType,url);
            rpc.sendInnerMsg(jsonMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(KFRpcType kfRpcType, String url, HMProtobuf.HMRequest msg) {
        try {
            IRPC rpc = getAndStartRpc(kfRpcType,url);
            rpc.sendPBRequestMsg(msg.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IRPC getAndStartRpc(KFRpcType kfRpcType,String url) {
        IRPC rpc = rpcMap.get(url);
        if(rpc == null) {
            rpc = startRpc(kfRpcType,url);
        }
        return rpc;
    }

    //启动rpc
    public IRPC startRpc(KFRpcType kfRpcType,String url) {
        String rpcUrl = url;
        if(!rpcUrl.startsWith("bolt")) {
            rpcUrl = "bolt://" + url;
        }
        IRPC rpc = kfRpcType.buildRpc(rpcUrl);
        rpcMap.put(url,rpc);
        //同步数据
        syncServer(url);
        return rpc;
    }

    public void syncServer(String url) {
        String ip = getInnerIp();
        if(StrUtil.isEmpty(ip)) {
            return;
        }
        JsonMsg msg = JsonMsg.create(MessageComm.G2C_SyncRpcServer);
        msg.addProperty("serverIds", GameServerManager.getInstance().getServerIdList());
        int rpcPort = ServerConfig.getInstance().getRpcPort();
        msg.addProperty("rpcUrl", ip+":"+rpcPort);
        IRPC rpc = rpcMap.get(url);
        try {
            rpc.sendInnerMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadAll() {
        String ip = getInnerIp();
        if(StrUtil.isEmpty(ip)) {
            return;
        }
        JsonMsg msg = JsonMsg.create(MessageComm.G2C_SyncRpcServer);
        msg.addProperty("serverIds", GameServerManager.getInstance().getServerIdList());
        int rpcPort = ServerConfig.getInstance().getRpcPort();
        msg.addProperty("rpcUrl", ip+":"+rpcPort);
        for (IRPC rpc : rpcMap.values()) {
            try {
                rpc.sendInnerMsg(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getInnerIp() {
        String ip = ServerConfig.getInstance().getHostName();
        if(StrUtil.isEmpty(ip)) {
            return null;
        }
        return GameIpUtils.getInnerIp(ip);
    }

}
