
package com.hm.server;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.server.BaseServer;

/**
 * Title: GameServerConifg.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author
 * @version 1.0
 */
public class ChatServer extends BaseServer {

    @Override
    public int getIdleSec() {
        return ServerConfig.getInstance().getIdelSec();
    }

    @Override
    public int getPort() {
        return ServerConfig.getInstance().getPort();
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
}

