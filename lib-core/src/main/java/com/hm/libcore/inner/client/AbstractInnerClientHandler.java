package com.hm.libcore.inner.client;

import com.google.common.collect.Maps;
import com.hm.libcore.handler.RequestHandler;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.protobuf.HMProtobuf;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public abstract class AbstractInnerClientHandler extends RequestHandler {

	
	private Map<Integer,String> msgMethodMap = Maps.newHashMap();
	
	@Override
	public void messageReceived(HMSession session, HMProtobuf.HMRequest req) {
		try{
			String method = msgMethodMap.get(req.getMsgId());
			JsonMsg jsonMsg = new JsonMsg(req.getMsgId());
			jsonMsg.initHMRequest(req);
			this.getClass().getMethod(method, HMSession.class,JsonMsg.class).invoke(this,session,jsonMsg);
		}catch(Exception e){
			log.error("req = " + req,e);
		}
	}
	
	public void sendMsg(JsonMsg msg) {
		getSession().sendMsg(msg.createRequest());
	}
	
	
	public abstract BaseInnerClient getSession();
	
	public void registMethod(int msgId,String method){
		msgMethodMap.put(msgId,method);
	}
	
	@Override
	public void sessionClosed(HMSession session) {
	}

	@Override
	public void sessionCreated(HMSession session) {
	}

	@Override
	public void sessionIdel(HMSession session) {
	}

	@Override
	public void exceptionCaught(HMSession session) throws Exception {
	}
	
	
}
