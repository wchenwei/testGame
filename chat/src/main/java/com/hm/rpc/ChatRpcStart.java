package com.hm.rpc;

import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import com.hm.libcore.rpc.IChatRpc;
import lombok.extern.slf4j.Slf4j;

/**
 * 跨服消费rpc启动
 * @Author ljfeng
 * @Date 23-11-29
 **/
@Slf4j
public class ChatRpcStart {

    public static void startRpc() {
        ServerConfig serverConfig = new ServerConfig()
                .setProtocol("bolt") // 设置一个协议，默认bolt
                .setPort(com.hm.libcore.serverConfig.ServerConfig.getInstance().getPort()) // 设置一个端口，默认12200
                .setDaemon(false); // 非守护线程

        ProviderConfig<IChatRpc> providerConfig = new ProviderConfig<IChatRpc>()
                .setInterfaceId(IChatRpc.class.getName()) // 指定接口
                .setRef(new ChatRpc()) // 指定实现
                .setServer(serverConfig); // 指定服务端

        providerConfig.export(); // 发布服务
        log.info("chat rpc start port : {}", serverConfig.getPort());
    }
}
