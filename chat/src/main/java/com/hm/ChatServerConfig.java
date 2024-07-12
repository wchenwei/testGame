package com.hm;

import com.hm.libcore.spring.SpringUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/12/8 17:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "gameconfig")
public class ChatServerConfig {
    private static ChatServerConfig instance = null;
    public static ChatServerConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(ChatServerConfig.class);
        }
        return instance;
    }
    private String chatcheckkey;
    private boolean badWordSave;
    private int innerPort;

}
