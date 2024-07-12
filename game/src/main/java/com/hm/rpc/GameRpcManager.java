package com.hm.rpc;

import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.rpc.IRPC;
import com.hm.libcore.rpc.SFRpcConf;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class GameRpcManager {
    public Map<String, IGameRpc> rpcMap = Maps.newConcurrentMap();

    //发送rpc消息
    public void sendInnerMsg(String url, JsonMsg jsonMsg) {
        try {
            IGameRpc rpc = getAndStartRpc(url);
            rpc.sendInnerMsg(jsonMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(String url, HMProtobuf.HMRequest msg) {
        try {
            IGameRpc rpc = getAndStartRpc(url);
            rpc.sendPBRequestMsg(msg.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerHMResponse(long playerId,String url, HMProtobuf.HMResponse response) {
        try {
            IGameRpc rpc = getAndStartRpc(url);
            rpc.sendPlayerHMResponse(playerId,response.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendGameMsg(String url, JsonMsg jsonMsg) {
        try {
            IGameRpc rpc = getAndStartRpc(url);
            rpc.sendGameMsg(jsonMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IGameRpc getAndStartRpc(String url) {
        IGameRpc rpc = rpcMap.get(url);
        if(rpc == null) {
            rpc = startRpc(url);
        }
        return rpc;
    }

    //启动rpc
    public IGameRpc startRpc(String url) {
        String rpcUrl = url;
        if(!rpcUrl.startsWith("bolt")) {
            rpcUrl = "bolt://" + url;
        }
        ConsumerConfig<IGameRpc> consumerConfig = new ConsumerConfig<IGameRpc>()
                .setInterfaceId(IGameRpc.class.getName()) // 指定接口
                .setRepeatedReferLimit(SFRpcConf.repeatedReferLimit)
                .setTimeout(SFRpcConf.timeout)
                .setProtocol("bolt") // 指定协议
                .setDirectUrl(rpcUrl); // 指定直连地址
        IGameRpc gameRpc = consumerConfig.refer();
        rpcMap.put(url,gameRpc);
        return gameRpc;
    }
}
