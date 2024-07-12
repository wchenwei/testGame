package com.hm.main;

import com.hm.rpc.ChatRpcStart;
import com.hm.server.GameHttpServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatStart {
    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        ServerInit.initServer();
        try {
            ChatRpcStart.startRpc();

            GameHttpServer httpServer = new GameHttpServer();
            httpServer.start();

            log.info("聊天服务器启动成功");
        } catch (InterruptedException e) {
            log.error("服务器启动失败", e);
        }
    }
}
