package com.hm.libcore.db.mongo;

public class DBEntity <T>{
	private T id;
	private int serverId;//serverId

	public DBEntity() {
		super();
	}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}
	
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
