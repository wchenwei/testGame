package com.hm.model.serverpublic;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;

public class ServerDataDbUtils {
	public final static String ID = "serverdata";
	public static void saveOrUpdate(ServerData serverData) {
		MongodDB mongo = MongoUtils.getMongodDB(serverData.getServerId());
		mongo.update(serverData);
	}
	
	public static ServerData getServerData(int serverId) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		return mongo.get(ID, ServerData.class);
	}
	
}
