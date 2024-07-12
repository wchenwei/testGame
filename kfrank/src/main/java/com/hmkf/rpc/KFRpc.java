package com.hmkf.rpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.hm.actor.ActorDispatcherType;
import com.hm.libcore.actor.IRunner;
import com.hm.libcore.inner.InnerMsgRouter;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.rpc.IKFRpc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KFRpc implements IKFRpc {

    @Override
    public void sendInnerMsg(JsonMsg msg) {
        long playerId = msg.getPlayerId();
        ActorDispatcherType.Msg.putTask(playerId,new IRunner() {
            @Override
            public Object runActor() {
                InnerMsgRouter.getInstance().process(msg);
                return null;
            }
        });
    }

    @Override
    public void sendPBRequestMsg(byte[] data) {
        try {
            HMProtobuf.HMRequest msg = HMProtobuf.HMRequest.parseFrom(data);
            JsonMsg clientMsg = new JsonMsg(msg.getMsgId());
            clientMsg.initHMRequest(msg);

            sendInnerMsg(clientMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startRpc() {
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(com.hm.libcore.serverConfig.ServerConfig.getInstance().getPort()) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程

        ProviderConfig<IKFRpc> providerConfig = new ProviderConfig<IKFRpc>()
                .setInterfaceId(IKFRpc.class.getName()) // 指定接口
                .setRef(new KFRpc()) // 指定实现
                .setServer(serverConfig); // 指定服务端

        // 增加jvm关闭事件
//        if (RpcConfigs.getOrDefaultValue(RpcOptions.JVM_SHUTDOWN_HOOK, true)) {
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    log.error("SOFA RPC Framework catch JVM shutdown event, Run shutdown hook now.");
//                    System.out.println();
//                    RpcRuntimeContext.destroy();
//                }
//            }, "SOFA-RPC-ShutdownHook"));
//        }

        providerConfig.export(); // 发布服务
        log.info("kf rpc start port : {}", serverConfig.getPort());
    }

}
