package com.hmkf.main;

import cn.hutool.core.util.StrUtil;
import com.hm.main.spring.IStartHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认的启动定时器
 */
@Component
@Slf4j
public class KFServerStartHandler implements IStartHandler {
    @Override
    public void doStartServer() {
        try {
            log.info("===============================服务器开始启动===============================");
            if (StrUtil.isEmpty(System.getProperty("catalina.home"))) {
                System.setProperty("catalina.home", ".");
            }
            KFServerInit.initServer();
            KFServerInit.startGame();
            log.info("跨服服务器启动成功");
        } catch (Exception e) {
            log.error("跨服服务器启动失败", e);
        }
    }

    @Override
    public void doStopServer() {

    }
}
