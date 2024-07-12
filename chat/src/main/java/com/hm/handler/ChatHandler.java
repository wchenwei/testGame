
package com.hm.handler;

import com.hm.libcore.handler.BaseHandler;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.ChatBiz;
import com.hm.actor.ActorDispatcherType;
import com.hm.handler.task.ServiceTask;
import com.hm.model.Player;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicLong;


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
public class ChatHandler extends BaseHandler {
    //连接创建数量
    public static final AtomicLong connNum = new AtomicLong();

    @Override
    public void exceptionCaught(HMSession session) throws Exception {
        super.exceptionCaught(session);
    }

    @Override
    public void messageReceived(HMSession session, HMRequest msg) {
        HMProtobuf.HMRequest req = (HMProtobuf.HMRequest) msg;
        session.setAttribute("online", true);
        session.removeAttribute("timeout");
        long connId = session.getConnId();
        if (connId < 0) {
            return;
        }
        ServiceTask serviceTask = new ServiceTask(session, msg);
        ActorDispatcherType.Msg
                .putTask(connId, serviceTask);
    }

    @Override
    public void sessionClosed(HMSession session) {
        try {
            session.setConnId(-1L);
            session.setAttribute("online", false);
            session.setAttribute("timeout", System.currentTimeMillis() + 60 * 60 * 1000);//5分钟超时

            Player player = (Player) session.getAttachment();
            if (player != null) {
                ChatBiz loginBiz = SpringUtil.getBean("ChatBiz");
                loginBiz.doLoginOut(player);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void sessionCreated(HMSession session) {
        long connId = connNum.getAndIncrement() % ServerConfig.getInstance().getActionDispatcherNum();
        session.setConnId(connId);
        session.setAttribute("online", true);
        session.removeAttribute("timeout");

    }

    @Override
    public void sessionIdel(HMSession session) {
        super.sessionIdel(session);
    }


}

