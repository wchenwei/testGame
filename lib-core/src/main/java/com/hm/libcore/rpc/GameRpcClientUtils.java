package com.hm.libcore.rpc;

import cn.hutool.core.util.StrUtil;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 跨服生产消息到游戏服
 */
@Slf4j
public class GameRpcClientUtils {
    private static Map<String, IGameRpc> gameRpcMap = Maps.newHashMap();
    public static Map<Integer, String> serverMap = Maps.newHashMap();

    /**
     * 跨服生产者rpc启动
     * @param rpcUrl
     */
    public static void startMachineRpc(String rpcUrl) {
        try {
            if(gameRpcMap.containsKey(rpcUrl)) {
                return;
            }
            String url = "bolt://" + rpcUrl;
            ConsumerConfig<IGameRpc> consumerConfig = new ConsumerConfig<IGameRpc>()
                    .setInterfaceId(IGameRpc.class.getName()) // 指定接口
                    .setRepeatedReferLimit(SFRpcConf.repeatedReferLimit)
                    .setTimeout(SFRpcConf.timeout)
                    .setProtocol("bolt") // 指定协议
                    .setDirectUrl(url); // 指定直连地址

            log.info("game start product rpc url : {}", url);
            gameRpcMap.put(rpcUrl, consumerConfig.refer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IGameRpc getGameRpc(String rpcUrl) {
        IGameRpc rpc = gameRpcMap.get(rpcUrl);
        if(rpc == null) {
            startMachineRpc(rpcUrl);
            return gameRpcMap.get(rpcUrl);
        }
        return rpc;
    }


    public static IGameRpc getGameRpcByServerId(int serverId) {
        String url = serverMap.get(serverId);
        if(StrUtil.isEmpty(url)) {
            System.out.println(serverId+"找不到rpc game");
            return null;
        }
        return getGameRpc(url);
    }


    public static void changeServerRpc(int serverId,String url) {
        System.out.println(serverId+"->"+url);
        serverMap.put(serverId,url);
    }
}
