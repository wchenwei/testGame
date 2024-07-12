package com.hm.libcore.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ServerGameUrlConfig")
public class ServerGameUrlConfig {
	@Id
	private int id;
	@Field("dbURL")
	private String dbUrl;
	
	public ServerGameUrlConfig() {
		super();
	}
	public ServerGameUrlConfig(int serverId, String dbUrl) {
		super();
		this.id = serverId;
		this.dbUrl = dbUrl;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	public static String getDBUrl(int serverId) {
		ServerGameUrlConfig serverGameUrlConfig = MongoUtils.getLoginMongodDB().get(serverId, ServerGameUrlConfig.class);
		if(serverGameUrlConfig != null) {
			return serverGameUrlConfig.getDbUrl();
		} 
		return null;
	}

}
