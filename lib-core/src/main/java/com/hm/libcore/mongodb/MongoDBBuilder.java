package com.hm.libcore.mongodb;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.db.mongo.MongoPoolConfig;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 数据库构建
 * @author siyunlong  
 * @date 2019年11月16日 上午9:32:03 
 * @version V1.0
 */
public class MongoDBBuilder {

	public static Map<String, MongoClient> dbMap = Maps.newConcurrentMap();
	//key:数据库名称  value:数据库连接
	public static Map<String, MongoTemplate> databaseMap = Maps.newConcurrentMap();
	public MongoClient loginClient;

	public static synchronized MongoTemplate getMongoTemplate(String mongoUrl, String databaseName) {
		MongoTemplate template = databaseMap.get(databaseName);
		if (template == null) {
			template = new MongoTemplate(getGameMongoClient(mongoUrl), databaseName);
			databaseMap.put(databaseName, template);
		}
		return template;
	}

	public synchronized MongoClient getGameMongoClient(int serverId) {
		String dbUrl = ServerDBConfig.getGameServerDBUrl(serverId);
		if(StrUtil.isEmpty(dbUrl)) {
			//找不到db数据库,默认走登录服务器
			dbUrl = MongoUtils.getDefault_mongo_uri();
		}
		System.err.println("==============="+serverId+"游戏服数据库serverId:"+dbUrl);
		return getGameMongoClient(dbUrl);
	}

	public static synchronized MongoClient getGameMongoClient(String mongoUrl) {
		mongoUrl = checkDBUrl(mongoUrl);
		MongoClient mongoClient = dbMap.get(mongoUrl);
		if (mongoClient == null) {
			mongoClient = buildMongoClinet(100, mongoUrl);
			System.err.println("==========添加:" + mongoUrl);
			dbMap.put(mongoUrl, mongoClient);
		}
		return mongoClient;
	}

	public synchronized MongoClient getLoginMongoClient(String mongoUrl) {
		if(this.loginClient != null) {
			return loginClient;
		}
		mongoUrl = checkDBUrl(mongoUrl);
		this.loginClient = buildMongoClinet(10, mongoUrl);
		return this.loginClient;
	}

	public static String checkDBUrl(String mongoUrl) {
		if (StrUtil.startWith(mongoUrl, "mongodb://")) {
			return mongoUrl;
		}
		return "mongodb://" + mongoUrl;
	}

	public static MongoClient buildMongoClinet(int connectionsPerHost, String mongoUrl) {
		MongoPoolConfig poolConfig = MongoPoolConfig.getInstance();
		ConnectionPoolSettings poolSetting= ConnectionPoolSettings.builder()
				.maxSize(poolConfig.getMaxSize())
				.minSize(poolConfig.getMinSize())
				.maxWaitTime(poolConfig.getMaxWaitTime(), TimeUnit.MINUTES)
				.maxConnectionIdleTime(poolConfig.getMaxConnectionIdleTime(),TimeUnit.MINUTES)
				.maxConnectionLifeTime(poolConfig.getMaxConnectionLifeTime(), TimeUnit.SECONDS)
				.maintenanceFrequency(poolConfig.getMaintenanceFrequency(), TimeUnit.MINUTES)
				.maintenanceInitialDelay(poolConfig.getMaintenanceInitialDelay(), TimeUnit.MINUTES)
				.build();

		MongoClientSettings build = MongoClientSettings.builder()
				.applyToConnectionPoolSettings(builder -> builder.applySettings(poolSetting))
				.applyConnectionString(new ConnectionString(mongoUrl)).build();
		MongoClient client=new MongoClientImpl(build,null);
		return client;
	}
}
