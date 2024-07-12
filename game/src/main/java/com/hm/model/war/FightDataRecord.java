package com.hm.model.war;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.war.sg.WarResult;

//战斗数据存储
public class FightDataRecord extends DBEntity<String>{
	private WarResult result;
	
	public FightDataRecord(String id,int serverId,WarResult result) {
		setId(id);
		setServerId(serverId);
		this.result = result;
	}

	public FightDataRecord() {
		super();
	}
	
	public WarResult getResult() {
		return result;
	}
	
	public void saveDb() {
		MongodDB mongo = MongoUtils.getMongodDB(getServerId());
		saveDbByMongoDb(mongo, "FightDataRecord");
	}
	
	public void saveDbByMongoDb(MongodDB mongo,String collName) {
		mongo.save(this,collName);
	}
}
