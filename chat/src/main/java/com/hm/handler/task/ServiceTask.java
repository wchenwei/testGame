/**
 *
 */
package com.hm.handler.task;

import com.hm.libcore.actor.IRunner;
import com.hm.libcore.msg.Router;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.handler.HMSession;
import lombok.extern.slf4j.Slf4j;

/**
 * Title: ServiceTask.java Description:业务线程池处理的任务 Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 叶亮
 * @date 2015年8月6日 下午1:45:40
 * @version 1.0
 */
@Slf4j
public class ServiceTask implements Runnable, IRunner {
    private HMSession session;
    private HMRequest req;

    public ServiceTask(HMSession session, HMRequest req) {
        this.session = session;
        this.req = req;
    }

    @Override
    public Object runActor() {
        run();
        return null;
    }

    @Override
    public void run() {
        debugRecv();
        try {
            Router.getInstance().process(req, session, false);
        } catch (Throwable e) {
            log.error("execute task thread:" + Thread.currentThread().getName() + " error", e);
        }
    }

    private void debugRecv() {
        if (ServerConfig.getInstance().isDebug()) {
            log.info(session.getRemoteAddress() + "getMsg:// msgID:" + this.req.getMsgId() + "=" + this.req.getData().toStringUtf8());
        }
    }


}
