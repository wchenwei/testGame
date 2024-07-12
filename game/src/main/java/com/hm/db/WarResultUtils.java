package com.hm.db;

import com.hm.model.war.BattleRecord;
import com.hm.model.war.FightDataRecord;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;

public class WarResultUtils {
	public final static String TableName = "BattleRecord";
	public static void saveOrUpdate(BattleRecord data) {
		MongodDB mongo = MongoUtils.getMongodDB(data.getServerId());
		mongo.save(data,TableName);
	}
	
	public static BattleRecord getBattleRecord(int serverId,String id) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		return mongo.get(id, BattleRecord.class,TableName);
	}
	
	public static FightDataRecord getFightDataRecord(int serverId,String id) {
		MongodDB mongo = MongoUtils.getMongodDB(serverId);
		return mongo.get(id, FightDataRecord.class,"FightDataRecord");
	}
}
