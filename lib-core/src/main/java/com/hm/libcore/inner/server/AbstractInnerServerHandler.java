package com.hm.libcore.inner.server;

import com.google.common.collect.Maps;
import com.hm.libcore.handler.RequestHandler;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.protobuf.HMProtobuf;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractInnerServerHandler extends RequestHandler{
	private static final String ServerId = "serverId";
	//key:服务器id  value:服务器socket
	private Map<Integer, HMSession> sessionMap = new ConcurrentHashMap<>();

	private Map<Integer,String> msgMethodMap = Maps.newHashMap();
	
	public void registMethod(int msgId,String method){
		msgMethodMap.put(msgId,method);
	}
	
	public void registClient(HMSession session, HMProtobuf.HMRequest req) {
		int serverId = JSONUtil.fromJson(req.getData().toStringUtf8(), Integer.class);
		HMSession oldSession = sessionMap.get(serverId);
		if(oldSession != session) {
			session.setAttribute(ServerId, serverId);
			sessionMap.put(serverId, session);
			log.error("获得内网连接："+serverId);
		}
	}
	
	
	
	public void sendMsg(HMSession session, JsonMsg msg) {
		session.write(msg.createRequest());
	}
	public void sendMsg(int serverId,JsonMsg msg) {
		HMSession session = sessionMap.get(serverId);
		if(session != null) {
			sendMsg(session, msg);
		}else{
			log.error("HMSession-serverId="+serverId+"不存在");
		}
	}
	
	public Map<Integer, HMSession> getSessionMap() {
		return sessionMap;
	}
	@Override
	public void sessionClosed(HMSession session) {
		if(session.containsAttrKey(ServerId)) {
			int serverId = (int)session.getAttribute(ServerId);
			sessionMap.remove(serverId);
			log.error("内网服务器id="+serverId+"断开连接");
		}
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
