package com.hm.main.spring;

import cn.hutool.core.util.StrUtil;
import com.hm.main.ChatStart;
import com.hm.main.ServerInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("===============================服务器开始启动===============================");
            if (StrUtil.isEmpty(System.getProperty("catalina.home"))) {
                System.setProperty("catalina.home", ".");
            }
            ChatStart.startServer();
            log.info("服务器启动成功");
        } catch (Exception e) {
            log.error("服务器启动失败", e);
        }
    }

}