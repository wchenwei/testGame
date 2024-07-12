
package com.hm.handler;

import com.hm.libcore.handler.BaseHandler;
import com.hm.libcore.handler.ServerStateCache;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.login.biz.LoginBiz;
import com.hm.actor.ActorDispatcherType;
import com.hm.handler.task.ServiceTask;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Title: GameHandler.java
 * Description:处理基类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年1月12日 下午3:20:48
 * @version 1.0
 */
@Slf4j
public class GameHandler extends BaseHandler{
	public static final AtomicLong connNum = new AtomicLong();


	
	protected <T> T getBean(String beanName){
		T t = SpringUtil.getBean(beanName);
		return t;
	}
	
	@Override
	public void exceptionCaught(HMSession session) throws Exception {
		super.exceptionCaught(session);
	}

	@Override
	public void messageReceived(HMSession session, HMRequest msg) {
		HMProtobuf.HMRequest req = (HMProtobuf.HMRequest)msg;
		session.setAttribute("online", true);
		session.removeAttribute("timeout");
		if(!session.containsAttrKey("playerId")){
//				if(!req.getAction().equals("LoginAction")){
//					session.write(HMRespCreater.createErrorHMresponse(req.getCallback(), SysConstant.SESSION_TIMEOUT));
//					return;
//				}
		}
		if(ServerStateCache.serverIsClose()) {
			session.sendErrorMsg(MessageComm.S2C_ErrorMsg, SysConstant.SERVER_CLOSE);
			return;
		}
		long connId = session.getConnId();
		if (connId < 0) {
			return;
		}
		ServiceTask serviceTask = new ServiceTask(session, msg);
		ActorDispatcherType.Msg.putTask(connId, serviceTask);
	}

	@Override
	public void sessionClosed(HMSession session) {
		try {
			session.setConnId(-1L);
			session.setAttribute("online", false);
			session.setAttribute("timeout", System.currentTimeMillis() + 60 * 60 * 1000);//5分钟超时
			
			Player player = SessionUtil.getPlayer(session);
			if(player != null) {
				LoginBiz loginBiz = SpringUtil.getBean(LoginBiz.class);
				loginBiz.doLoginOut(player);
			}
		} catch (Exception e) {
			session.setAttachment(null);
		}
	}

	@Override
	public void sessionCreated(HMSession session) {
		long connId = connNum.getAndIncrement() % ActorDispatcherType.Msg.getPoolSize();
		session.setConnId(connId);

		session.setAttribute("online", true);
		session.removeAttribute("timeout");
		
	}

	@Override
	public void sessionIdel(HMSession session) {
		super.sessionIdel(session);
	}
	
	
	
}

