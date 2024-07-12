package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.server.GameServerManager;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class ServerUtils {
	public static final int MaxId = 100000;
	public static int getServerId(long playerId) {
		return GameServerManager.getInstance().getDbServerId(getCreateServerId(playerId));
	}

	public static long getMinPlayerId(long serverId) {
		return serverId * MaxId;
	}

	public static long getMaxPlayerId(long serverId) {
		return serverId * MaxId + (long) (MaxId * 0.9);
	}

	public static int getCreateServerId(long playerId) {
		return (int) (playerId / MaxId);
	}
    
    /**
     * 是否合服过
     *
     * @param serverId
     * @return
     */
    public static boolean isMergeServer(int serverId) {
        return ServerDataManager.getIntance().getServerData(serverId).getServerMergeData().getDate() > 0;
    }
    
    /**
     * 是否是此服务器的玩家
     * @param serverId
     * @param playerId
     * @return
     */
    public static boolean isServerPlayer(int serverId,long playerId) {
    	return getServerId(playerId) == serverId;
    }
    
    /**
     * 
     * @param serverId
     * @return
     */
    public static ServerInfo getServerInfo(int serverId) {
		Query query = new Query();
    	query.addCriteria(Criteria.where("server_id").is(serverId));
    	List<ServerInfo> resultList = MongoUtils.getLoginMongodDB().query(query, ServerInfo.class);
    	if(CollUtil.isNotEmpty(resultList)) {
    		return resultList.get(0);
    	}
    	return null;
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
	
	public static Map<String,Object> getServerInfoForMap(int serverId) {
		Query query = new Query();
    	query.addCriteria(Criteria.where("server_id").is(serverId));
    	List<Map> resultList = MongoUtils.getLoginMongodDB().query(query, Map.class,"serverInfo");
    	if(CollUtil.isNotEmpty(resultList)) {
    		return resultList.get(0);
    	}
    	return null;
	}
	
	public static void main(String[] args) {
		System.err.println(getMaxPlayerId(2));
		System.err.println(getMinPlayerId(2));
	}
}
