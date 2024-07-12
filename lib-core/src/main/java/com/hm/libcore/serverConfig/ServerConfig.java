package com.hm.libcore.serverConfig;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gameconfig")
public final class ServerConfig {
    private static ServerConfig instance = null;

    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(ServerConfig.class);
        }
        return instance;
    }

    private int machineId;//机器id
    private String hostName;
    private int servertype;//服务器类型  0-游戏服 1-聊天服
    private int port;//主服务器端口
    private int maxThreadCount;
    private int idelSec = 600;
    private int writeTimeout = 600;
    private int readTimeout = 600;
    private String requestHandler = "BaseHandler";
    private boolean debug = false;
    private boolean monitor = false;//监控
    private int httpPort;
    private int httpMaxThreadCount;
    private int httpIdel = 60;
    private int httpWriteIdel = 60;
    private int httpReadIdel = 60;
    private int ActionDispatcherNum = 32;//消息处理器数量
    private String downloadUrl;
    private String gmUrl;
    private String redisUrl;
    private String loginUrl;
    private boolean testPay;
    private int rebateType;//充值返利类型 0既不记录也不发放 1-记录充值  2-发放
    private String rankServerUrl;//排行服务器地址
    private String chatcheckkey;//聊天校验key
    private boolean useGm = false;//是否可以用GM命令
    private boolean sendChEvent = false;//是否发送草花事件
    private boolean sendChRankEvent = false;//是否发送草花活动排行事件
    private int language = 1;//语言
    private String startHandler;//启动处理器
    private int rpcPort = 12200;
    private String payPercent;
    private boolean idCodeSwitch;
    private boolean IpFilterSwitch;
    private boolean roleNumLimit;
    private int envtype = 0;//0-内网  1-腾讯云环境

    /**
     * 是否能够使用gm命令
     */
    public boolean getUseGmOrder(int serverId) {
        return useGm;
    }

    public boolean isGameServer() {
        return this.servertype == 0;
    }

    public String getKFDBName() {
        String ip = getHostName();
        return ip.replace(".", "_")+"_"+getPort();
    }
}
