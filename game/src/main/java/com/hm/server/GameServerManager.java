package com.hm.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.mongodb.ServerGroup;
import com.hm.util.ServerUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class GameServerManager {
	private static GameServerManager instance = new GameServerManager();
	public static GameServerManager getInstance() {
		return instance;
	}

	//此服务器运行的游戏服务器id列表
	private Map<Integer,Integer> serverMap = Maps.newConcurrentMap();
	private Map<Integer,ServerInfo> serverInfoMap = Maps.newConcurrentMap();
	//真实的服务器列表
	private List<Integer> serverList = Lists.newArrayList();

	public void init() {
		Map<Integer,Integer> serverMap = Maps.newConcurrentMap();
		Map<Integer,ServerInfo> tempServerInfoMap = Maps.newConcurrentMap();
		List<ServerInfo> serverinfoList = MongoUtils.getServerList();
		for (ServerInfo serverInfo : serverinfoList) {
			serverMap.put(serverInfo.getServer_id(), serverInfo.getDb_id());
			tempServerInfoMap.put(serverInfo.getServer_id(), serverInfo);
		}
		this.serverMap = serverMap;
		this.serverInfoMap = tempServerInfoMap;
		this.serverList = MongoUtils.getDbIdList();
		System.err.println("服务器数据库对应："+this.serverMap);
	}

	public List<Integer> getServerIdList() {
		return this.serverList;
	}

	public Map<Integer, Integer> getServerMap() {
		return serverMap;
	}

	public void addServerId(int serverId) {
		if(!containServer(serverId)) {
			init();
		}
	}

	public int getDbServerId(int serverId) {
		int dbId = this.serverMap.getOrDefault(serverId, 0);
		return dbId > 0?dbId:serverId;
	}

	/**
	 * 包括合服的服务器
	 * @param serverId
	 * @return
	 */
	public boolean containServer(int serverId) {
		return this.serverMap.containsKey(serverId);
	}

	/**
	 * 是否是真的服务器
	 * @param serverId
	 * @return
	 */
	public boolean isDbServer(int serverId) {
		return this.serverList.contains(serverId);
	}
	public boolean isServerMachinePlayer(long playerId) {
		return containServer(ServerUtils.getCreateServerId(playerId));
	}

	public boolean removeServer(Integer serverId) {
		this.serverMap.remove(serverId);
		this.serverList.remove(serverId);
		return true;
	}

	public ServerGroup getServerGroup(int serverId) {
		ServerInfo serverInfo = getServerInfoFromDB(getDbServerId(serverId));
		if(serverInfo == null || serverInfo.getServer_groupid() <= 0) {
			return null;
		}
		int groupId = serverInfo.getServer_groupid();
		return MongoUtils.getLoginMongodDB().get(groupId, ServerGroup.class,"servergroup");
	}

	public static ServerInfo getServerInfoFromDB(int serverId) {
    	return ServerInfo.getServerInfoFromDB(serverId);
	}

	public static List<ServerInfo> getAllServerInfoFromDB() {
    	return MongoUtils.getLoginMongodDB().queryAll(ServerInfo.class);
	}
	public static List<ServerInfo> getServerInfoByType(int type) {
		Query query = new Query();
    	query.addCriteria(Criteria.where("server_typeid").is(type));
    	return MongoUtils.getLoginMongodDB().query(query, ServerInfo.class);
	}

	public static List<ServerInfo> getMergeServerInfo(int serverId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		Criteria criteria1 = Criteria.where("db_id").is(serverId);
		Criteria criteria2 = Criteria.where("server_id").is(serverId);
    	query.addCriteria(criteria.orOperator(criteria1, criteria2));
    	List<ServerInfo> resultList = MongoUtils.getLoginMongodDB().query(query, ServerInfo.class);
    	return resultList;
	}

	public void putServerDbMap(Map<Integer,Integer> dbMap) {
		this.serverMap.putAll(dbMap);
	}
	//判断是否是测试服务器
	public boolean isTestServer(int serverid) {
		if(serverInfoMap.containsKey(serverid) && !serverInfoMap.get(serverid).isTestServer()) {
			return false;
		}
		return true;
	}
}

