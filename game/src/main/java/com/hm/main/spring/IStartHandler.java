package com.hm.main.spring;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;

public interface IStartHandler {
    void doStartServer();
    void doStopServer();

    public static IStartHandler getServerStartHandler() {
        String startHandler = ServerConfig.getInstance().getStartHandler();
        if(StrUtil.isEmpty(startHandler)) {
            return SpringUtil.getBean(DefaultStartHandler.class);
        }
        return SpringUtil.getBean(startHandler);
    }
}
