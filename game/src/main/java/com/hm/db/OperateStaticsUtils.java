package com.hm.db;

import com.hm.action.http.gm.LvDistribution;
import com.hm.action.http.gm.OnlineStatistics;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;

public class OperateStaticsUtils {
	public static OnlineStatistics getOnlineNumStatics(int serverId,String nowDate) {
		MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
        return mongodDB.get(nowDate, OnlineStatistics.class);
    }
	
	public static void saveOrUpdateOnlineStatics(int serverId,OnlineStatistics onlineStatics) {
		MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
		mongodDB.update(onlineStatics);
	}
	public static LvDistribution getLvDistribution(int serverId,String nowDate) {
		MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
        return mongodDB.get(nowDate, LvDistribution.class);
    }
	
	public static void saveOrUpdateLvDistribution(int serverId,LvDistribution lvDistribution) {
		MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
		mongodDB.update(lvDistribution);
	}

}
