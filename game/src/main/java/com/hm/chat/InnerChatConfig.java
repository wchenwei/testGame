package com.hm.chat;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Slf4j
@Document(collection = "chatserver")
public class InnerChatConfig {

    @Field("clientUrl")
    private String clientIp;
    @Field("host")
    private String host;
    @Field("tcpPort")
    private int port;

    public static InnerChatConfig getInnerChatConfig() {
        if(!ServerConfig.getInstance().isGameServer()) {
            return null;
        }
        GameServerMachine.checkGameServerMachineId();
        int machineId = ServerConfig.getInstance().getMachineId();
        if (machineId <= 0) {
            log.error("加载聊天服务器出错:machineId:" + machineId);
            return null;
        }
        GameServerMachine serverMachine = GameServerMachine.getCurGameServerMachine();
        if (serverMachine == null) {
            log.error("加载聊天服务器出错:找不到machineId:" + machineId);
            return null;
        }
        int chatServerId = serverMachine.getChatserver();
        return MongoUtils.getLoginMongodDB().get(chatServerId, InnerChatConfig.class);
    }
}
