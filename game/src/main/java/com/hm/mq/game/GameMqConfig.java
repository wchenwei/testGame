package com.hm.mq.game;

import com.hg.mq.MqConfig;
import com.hm.libcore.spring.SpringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mq配置
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/8/11 14:54
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "mq")
public class GameMqConfig extends MqConfig {
    private String platformTopic;
    private String rankTopic;

    private static GameMqConfig instance = null;

    public static GameMqConfig getInstance() {
        if (instance == null) {
            instance = SpringUtil.getBean(GameMqConfig.class);
        }
        return instance;
    }

    public static void initMqConfig() {
        MqConfig.setInstance(getInstance());
    }

}
