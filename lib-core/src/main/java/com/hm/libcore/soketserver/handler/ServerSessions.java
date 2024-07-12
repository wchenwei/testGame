package com.hm.libcore.soketserver.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title: ServerSessions.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-8-8
 * @version 1.0
 */
public class ServerSessions {
	
	private static ServerSessions serverSessions = null;
	
	
	public static ServerSessions getInstance(){
		if(serverSessions == null){
			serverSessions = new ServerSessions();
		}
		return serverSessions;
	}
	
	
	private Map<String,HMSession> sessions;
	
	private ServerSessions(){
		this.sessions = new ConcurrentHashMap<String, HMSession>();
	}
	
	
	public HMSession get(String id){
		return this.sessions.get(id);
	}
	
	public void put(String id,HMSession session){
		this.sessions.put(id, session);
	}
	
	public HMSession remove(String id){
		return this.sessions.remove(id);
	}
	
	public int count(){
		return this.sessions.size();
	}

	public boolean contains(String id){
		return this.sessions.containsKey(id);
	}


	public Map<String, HMSession> getSessions() {
		return sessions;
	}
	
}
