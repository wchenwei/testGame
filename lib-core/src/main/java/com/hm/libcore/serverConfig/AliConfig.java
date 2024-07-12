package com.hm.libcore.serverConfig;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: yang xb
 * Date: 2018-06-11
 */

@Data
@Component
@ConfigurationProperties(prefix = "ali")
public class AliConfig {
    private String endpoint;
    private String projectName;
    private String accessKeyId;
    private String accessKey;

    private static AliConfig instance = null;

    public static AliConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(AliConfig.class);
        }
        return instance;
    }
}
