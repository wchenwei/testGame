package com.hm.libcore.inner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hm.libcore.soketserver.handler.HMSession;

public class InnerSessions {
	private static InnerSessions serverSessions = null;
	
	public static InnerSessions getInstance(){
		if(serverSessions == null){
			serverSessions = new InnerSessions();
		}
		return serverSessions;
	}
	
	
	private Map<String, HMSession> sessions;
	
	private InnerSessions(){
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
