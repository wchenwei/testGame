/**
 *
 */
package com.hm.server;

import com.hm.libcore.httpserver.server.HttpBaseServer;
import com.hm.libcore.serverConfig.ServerConfig;

/**
 * Title: GameHttpServer.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年7月21日 下午2:07:15
 * @version 1.0
 */
public class GameHttpServer extends HttpBaseServer {

    @Override
    public int getHttpMaxThreadCount() {
        return ServerConfig.getInstance().getHttpMaxThreadCount();
    }

    @Override
    public int getHttpPort() {
        return ServerConfig.getInstance().getHttpPort();
    }

    @Override
    public int getHttpIdel() {
        return ServerConfig.getInstance().getHttpIdel();
    }

    @Override
    public int getHttpWriteIdel() {
        return ServerConfig.getInstance().getHttpWriteIdel();
    }

    @Override
    public int getHttpReadIdel() {
        return ServerConfig.getInstance().getHttpReadIdel();
    }

}
