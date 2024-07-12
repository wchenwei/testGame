
package com.hm.server;

import com.hm.libcore.serverConfig.H5Config;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.websocket.server.BaseServer;

/**
 * Title: GameServerConifg.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author
 * @version 1.0
 */
public class WebSocketServer extends BaseServer {

    @Override
    public int getIdleSec() {
        return ServerConfig.getInstance().getIdelSec();
    }

    @Override
    public int getPort() {
        return ServerConfig.getInstance().getPort() + 1;
    }

    @Override
    public int getThreadCount() {
        return ServerConfig.getInstance().getMaxThreadCount();
    }

    @Override
    public int getWriteTimeOut() {
        return ServerConfig.getInstance().getWriteTimeout();
    }

    @Override
    public int getReadTimeOut() {
        return ServerConfig.getInstance().getReadTimeout();
    }

    @Override
    public String getSSLKeyPath() {
        return H5Config.getInstance().getSSLKeyPath();
    }

    @Override
    public String getSSLKeyPwd() {
        return H5Config.getInstance().getSSLKeyPwd();
    }
}

