package com.hm.libcore.serverConfig;

import com.hm.libcore.enums.EnvType;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 服务器游戏物理机
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 11:49
 */
@Data
@Document(collection = "serverMachine")
public class GameServerMachine {
    private int id;
    private String name;
    private String host;
    private int port;
    private int rpcPort=12200;
    private int chatserver;
    private int http_port;
    private int machineGroupId;


    public static GameServerMachine getCurGameServerMachine() {
        int id = ServerConfig.getInstance().getMachineId();
        GameServerMachine gameServerMachine = MongoUtils.getLoginMongodDB().get(id, GameServerMachine.class);
        return gameServerMachine;
    }


    public boolean isTestServer() {
        if (host.contains("127.0.0.1") || name.contains("版署")
                || name.contains("提审") || name.contains("马甲") || name.contains("过审")) {
            return true;
        }
        return false;
    }

    public static GameServerMachine getServerMachine(int id) {
        return MongoUtils.getLoginMongodDB()
                .get(id, GameServerMachine.class, "serverMachine");
    }


    /**
     * 检查当前服务器是否动态读取物理机id
     */
    public static void checkGameServerMachineId() {
        //查找查找动态配置文件
        if (ServerVariableConf.getInstance().getMachineId() > 0) {
            ServerConfig.getInstance().setMachineId(ServerVariableConf.getInstance().getMachineId());
            System.out.println("动态加载本机物理机器ID:" + ServerVariableConf.getInstance().getMachineId());
            return;
        }
        if (ServerConfig.getInstance().getMachineId() > 0) {
            return;
        }
        EnvType envType = EnvType.getType(ServerConfig.getInstance().getEnvtype());
        String ip = envType.getOutIp();
        System.out.println("本机IP:" + ip);
        Query query = new Query();
        query.addCriteria(Criteria.where("host").is(ip));
        GameServerMachine gameServerMachine = MongoUtils.getLoginMongodDB().queryOne(query, GameServerMachine.class);
        if (gameServerMachine == null) {
            throw new RuntimeException("本机服务器找不到所配置的物理机");
        }
        ServerConfig.getInstance().setMachineId(gameServerMachine.getId());
        System.out.println("腾讯云本机物理机器ID:" + gameServerMachine.getId());
    }

    public static void loadServerTcpPort() {
        GameServerMachine serverMachine = getCurGameServerMachine();
        ServerConfig.getInstance().setHostName(serverMachine.getHost());
        ServerConfig.getInstance().setPort(serverMachine.getPort());
        ServerConfig.getInstance().setHttpPort(serverMachine.getHttp_port());
        ServerConfig.getInstance().setRpcPort(serverMachine.getRpcPort());
    }

    public String getInnerIp() {
        return this.host;
    }

}
