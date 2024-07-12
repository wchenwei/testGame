package com.hm.libcore.mongodb;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @Description: 游戏服务器数据库配置
 * @author siyunlong  
 * @date 2019年11月16日 上午9:19:17 
 * @version V1.0
 */
@Document(collection = "ServerDBManage")
public class ServerDBConfig {
	@Id
	private String id;
	@Field("startServerId")
	private int startId;
	@Field("endServerId")
	private int endId;
	@Field("dbURL")
	private String dbUrl;
	
	
	public ServerDBConfig(int startId, int endId, String dbUrl) {
		this.id = PrimaryKeyWeb.getLoginStrIncrementKey("ServerDBConfig");
		this.startId = startId;
		this.endId = endId;
		this.dbUrl = dbUrl;
	}

	public ServerDBConfig() {
		super();
	}


	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public int getEndId() {
		return endId;
	}

	public void setEndId(int endId) {
		this.endId = endId;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public boolean isFitServer(int serverId) {
		return serverId >= startId && serverId <= endId;
	}
	
	public boolean isDefaut() {
		return this.startId == -1 && this.endId == -1;
	}
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	public static String getGameServerDBUrl(int serverId) {
		//根据serverID找url
		String dbUrl = ServerGameUrlConfig.getDBUrl(serverId);
		if(StrUtil.isNotEmpty(dbUrl)) {
			return dbUrl;
		}
		dbUrl = getGameServerDBUrlFromAll(serverId);
		new ServerGameUrlConfig(serverId, dbUrl).saveDB();
		return dbUrl;
	}
	
	public static String getGameServerDBUrlFromAll(int serverId) {
		List<ServerDBConfig> infoList = MongoUtils.getLoginMongodDB().queryAll(ServerDBConfig.class);
		if(CollUtil.isEmpty(infoList)) {
			return null;
		}
		ServerDBConfig luck = infoList.stream().filter(e -> e.isFitServer(serverId))
				.findFirst().orElse(null);
		if(luck != null) {
			return luck.getDbUrl();
		}
		luck = infoList.stream().filter(e -> e.isDefaut())
				.findFirst().orElse(null);
		if(luck != null) {
			return luck.getDbUrl();
		}
		return infoList.get(0).getDbUrl();
	}
	
}
