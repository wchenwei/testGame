
package com.hmkf.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hm.libcore.handler.BaseHandler;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.spring.SpringUtil;
import com.hm.handler.task.ServiceTask;
import com.hm.message.MessageComm;
import com.hm.sysConstant.SysConstant;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.model.KFPlayer;


/**
 * Title: GameHandler.java
 * Description:处理基类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 *
 * @author 李飞
 * @version 1.0
 * @date 2015年1月12日 下午3:20:48
 */
@Slf4j
public class GameHandler extends BaseHandler {
    protected <T> T getBean(String beanName) {
        T t = SpringUtil.getBean(beanName);
        return t;
    }

    @Override
    public void exceptionCaught(HMSession session) throws Exception {
        super.exceptionCaught(session);
    }

    @Override
    public void messageReceived(HMSession session, HMRequest msg) {
        HMProtobuf.HMRequest req = (HMProtobuf.HMRequest) msg;
        session.setAttribute("online", true);
        session.removeAttribute("timeout");
        if (!session.containsAttrKey("playerId")) {
//				if(!req.getAction().equals("LoginAction")){
//					session.write(HMRespCreater.createErrorHMresponse(req.getCallback(), SysConstant.SESSION_TIMEOUT));
//					return;
//				}
        }
        if (ServerStateCache.serverIsClose()) {
            session.sendErrorMsg(MessageComm.S2C_ErrorMsg, SysConstant.SERVER_CLOSE);
            return;
        }
        //使用任务线程池处理业务逻辑
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) SpringUtil.getBean("serviceExecutor");
        executor.execute(new ServiceTask(session, req));
    }

    @Override
    public void sessionClosed(HMSession session) {
        try {
            session.setAttribute("online", false);
            session.setAttribute("timeout", System.currentTimeMillis() + 60 * 60 * 1000);//5分钟超时

            KFPlayer player = (KFPlayer) session.getAttachment();
            if (player != null) {
                KFPlayerContainer playerContainer = SpringUtil.getBean(KFPlayerContainer.class);
                playerContainer.removePlayer(player.getId());
            }
        } catch (Exception e) {
            session.setAttachment(null);
            e.printStackTrace();
        }
    }

    @Override
    public void sessionCreated(HMSession session) {
        session.setAttribute("online", true);
        session.removeAttribute("timeout");

    }

    @Override
    public void sessionIdel(HMSession session) {
        super.sessionIdel(session);
    }


}

