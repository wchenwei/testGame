package com.hm.libcore.serverConfig;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "h5")
public class H5Config {
    private String SSLKeyPath;
    private String SSLKeyPwd;
    private boolean SslServer;
    private boolean openH5;

    private static H5Config instance = null;

    public static H5Config getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(H5Config.class);
        }
        return instance;
    }


}
