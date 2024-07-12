package com.hm.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.ServerInfo;
import com.hm.libcore.mongodb.ServerGameUrlConfig;
import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckServerDBUtils {
	public static final String GamePre = MongoUtils.getGameDBName();
	
	public static void main(String[] args) {
		for (String err : CheckServerDBUtils.checkDB()) {
			System.err.println(err+"<br>");
		}
	}
	
	public static List<String> checkDB() {
		List<String> errList = Lists.newArrayList();
		List<Integer> serverIds = getAllServerInfoFromDB().stream().map(e -> e.getServer_id()).collect(Collectors.toList());
		List<ServerGameUrlConfig> configList = getServerGameUrlConfig();
		Set<String> allMongoUrls = configList.stream().map(e -> e.getDbUrl()).collect(Collectors.toSet());
		Map<String,String> dbUrlMap = Maps.newHashMap();
		for (String url : allMongoUrls) {
			MongoClient client = MongoUtils.getMongoDBBuilder().getGameMongoClient(url);
			for (String dbName : client.listDatabaseNames()) {
				if(dbName.startsWith(GamePre)) {
					if(dbUrlMap.containsKey(dbName)) {
						errList.add(dbName+"="+url+"  err:"+dbUrlMap.get(dbName));
					}else{
						dbUrlMap.put(dbName, url);
					}
				}
			}
		}
		for (ServerGameUrlConfig serverGameUrlConfig : configList) {
			if(serverIds.contains(serverGameUrlConfig.getId())) {
				String dbUrl = dbUrlMap.get(GamePre+serverGameUrlConfig.getId());
				if(!StrUtil.equals(dbUrl, serverGameUrlConfig.getDbUrl())) {
					errList.add("url is error:"+serverGameUrlConfig.getId());
				}
			}
		}
		return errList;
	}
	
	
	public static List<ServerInfo> getAllServerInfoFromDB() {
		Query query = new Query();
    	query.addCriteria(Criteria.where("openstate").is(0).and("db_id").is(0));
    	return MongoUtils.getLoginMongodDB().query(query, ServerInfo.class);
	}
	public static List<ServerGameUrlConfig> getServerGameUrlConfig() {
		return MongoUtils.getLoginMongodDB().queryAll(ServerGameUrlConfig.class);
	}
}
