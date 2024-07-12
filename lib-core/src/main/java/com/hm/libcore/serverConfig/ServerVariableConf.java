package com.hm.libcore.serverConfig;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 服务器动态可配置参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "m")
@PropertySource(value = {"classpath:game.properties"}, ignoreResourceNotFound = true)
public class ServerVariableConf {
    public int machineId;
//    public boolean useGm = true;//是否可以使用GM
//    public int xxlPort;

    private static ServerVariableConf instance = null;

    public static ServerVariableConf getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(ServerVariableConf.class);
        }
        return instance;
    }
}
