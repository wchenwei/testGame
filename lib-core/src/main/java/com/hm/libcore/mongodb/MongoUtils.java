package com.hm.libcore.mongodb;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.spring.SpringUtil;
import com.mongodb.client.MongoClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class MongoUtils {
	private static String default_mongo_uri;
	private static String mongo_login_uri;
	private static String gameDBName;
	private static String loginDBName;
	private static String PayDBName;


	private static MongodDB loginMongoDB;
	@Getter
	private static MongodDB payMongoDB;
	//key:服务器id  value:数据库连接
	private static Map<Integer, MongoTemplate> gameDBMap = Maps.newConcurrentMap();
	//key:数据库名称  value:数据库连接
	private static Map<String, MongoTemplate> databaseMap = Maps.newConcurrentMap();

	private static MongoDBBuilder mongoDBBuilder = new MongoDBBuilder();

	static {
		Environment environment = SpringUtil.getBean(Environment.class);
		default_mongo_uri = environment.getProperty("mongo.mongo_uri");
		mongo_login_uri = environment.getProperty("mongo.mongo_login_uri");
		gameDBName = environment.getProperty("mongo.game_dbname_prefix");
		loginDBName = environment.getProperty("mongo.login_dbname");
		if (environment.containsProperty("mongo.pay_dbname")) {
			PayDBName = environment.getProperty("mongo.pay_dbname");
		}
		loginMongoDB = new MongodDB(new MongoTemplate(mongoDBBuilder.getLoginMongoClient(mongo_login_uri), loginDBName));
		if(StrUtil.isNotEmpty(PayDBName)) {
			payMongoDB = new MongodDB(new MongoTemplate(mongoDBBuilder.getLoginMongoClient(mongo_login_uri), PayDBName));
		}

		if (ServerConfig.getInstance().isGameServer()) {
			//聊天服加载数据
			GameServerMachine.checkGameServerMachineId();
			//加载所属服务器的数据库
			getDbIdList().forEach(e -> loadServerDbToCache(e));
			log.info("服务器数据库数量:" + gameDBMap.size());
		}
	}


	//获取当前服务器所有的服务器列表
	public static List<ServerInfo> getServerList() {
    	Query query = new Query();
    	int machineId = ServerConfig.getInstance().getMachineId();
    	query.addCriteria(Criteria.where("servermachineId").is(machineId));
    	return loginMongoDB.getMongoTemplate().find(query, ServerInfo.class,"serverInfo");
	}
	
	public static List<Integer> getDbIdList() {
		List<ServerInfo> serverInfos = getServerList();
		List<Integer> dbList = serverInfos.stream().map(e -> e.getServer_id()).collect(Collectors.toList());
		for (ServerInfo serverInfo : serverInfos) {
			if(serverInfo.getDb_id() > 0 && serverInfo.getDb_id() != serverInfo.getServer_id() && dbList.contains(serverInfo.getServer_id())) {
				dbList.remove(Integer.valueOf(serverInfo.getServer_id()));
			}
		}
		System.err.println("服务器数据库dbList:"+dbList);
		return dbList;
	}
	
	public static void loadServerDbToCache(int serverId) {
		gameDBMap.remove(serverId);
		MongoClient mongoClient= mongoDBBuilder.getGameMongoClient(serverId);
		gameDBMap.put(serverId, new MongoTemplate(mongoClient, gameDBName+serverId));
	}
	public static MongoTemplate getServerMongoTemplate(int serverId) {
		return gameDBMap.get(serverId);
	}
	
	public static MongodDB getMongodDB(String databaseName) {
		return getMongodDB(default_mongo_uri,databaseName);
	}
	public static MongodDB getMongodDB(String mongoUrl,String databaseName) {
		MongoTemplate template = databaseMap.get(databaseName);
		if(template == null) {
			template = new MongoTemplate(mongoDBBuilder.getGameMongoClient(mongoUrl), databaseName);
			databaseMap.put(databaseName, template);
		}
		return new MongodDB(template);
	}
	
	//此方法只回当前物理机的数据库连接
	public static MongodDB getMongodDB(int serverId) {
		MongoTemplate template = getServerMongoTemplate(serverId);
		if(template != null) {
			return new MongodDB(template);
		}
		return null;
	}
	//此方法可返回所有游戏数据库连接
	public static MongodDB getGameMongodDB(int serverId) {
		String databaseName = gameDBName+serverId;
		MongoTemplate template = databaseMap.get(databaseName);
		if(template != null) {
			return new MongodDB(template);
		}
		template = new MongoTemplate(mongoDBBuilder.getGameMongoClient(serverId), databaseName);
		databaseMap.put(databaseName, template);
		return new MongodDB(template);
	}
	
	public static MongodDB getLoginMongodDB() {
		return loginMongoDB;
	}

	public static MongoDBBuilder getMongoDBBuilder() {
		return mongoDBBuilder;
	}

	/**
	 * 获取serverId:serverName
	 *
	 * @return
	 */
	public static Map<Integer, String> serverNameInfo() {
		Map<Integer, String> m = Maps.newConcurrentMap();
		Consumer<Document> block = document -> {
			m.put(document.getInteger("server_id"), document.getString("name"));
		};
		loginMongoDB.getMongoTemplate().getCollection("serverInfo").find().
				projection(new Document("name", 1).append("server_id", 1)).forEach(block);
		return m;
	}
	
	public static String getDefault_mongo_uri() {
		return default_mongo_uri;
	}
	
	public static Map<String, MongoTemplate> getDatabaseMap() {
		return databaseMap;
	}

	public static Map<Integer, MongoTemplate> getGameDBMap() {
		return gameDBMap;
	}
	public static String getGameDBName() {
		return gameDBName;
	}

	public static void main(String[] args) {
		System.err.println(MongoUtils.getServerList());
	}
}
