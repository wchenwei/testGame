package com.hm.main.spring;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.main.ServerInit;
import com.hm.main.ServerStopBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认的启动定时器
 */
@Component
@Slf4j
public class DefaultStartHandler implements IStartHandler{
    @Override
    public void doStartServer() {
        try {
            log.info("===============================服务器开始启动===============================");
            if (StrUtil.isEmpty(System.getProperty("catalina.home"))) {
                System.setProperty("catalina.home", ".");
            }
            ServerInit.initServer();
            ServerInit.startGame();
            //启动rpc
            log.info("服务器启动成功");
        } catch (Exception e) {
            log.error("服务器启动失败", e);
        }
    }

    @Override
    public void doStopServer() {
        SpringUtil.getBean(ServerStopBiz.class).doStopServer();
    }
}
