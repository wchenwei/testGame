package com.hm.db;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
/**
 * 
 * ClassName: CommonDbUtil. <br/>  
 * Function: 公共db工具类. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年9月20日 下午7:37:47 <br/>  
 *  
 * @author zxj  
 * @version
 */
public class CommonDbUtil {
	public static MongodDB getMongoDB(int serverId) {
		return MongoUtils.getMongodDB(serverId);
	}
	
	@SuppressWarnings("rawtypes")
	public static void update(DBEntity dbEntity) {
		getMongoDB(dbEntity.getServerId()).update(dbEntity);
	}
	
	@SuppressWarnings("rawtypes")
	public static void insert(DBEntity dbEntity) {
		getMongoDB(dbEntity.getServerId()).insert(dbEntity);
	}
	
	public static<T> List<T> getList(Query query, Class<T> dbEntity, int serverId){
		return getMongoDB(serverId).query(query, dbEntity);
	}
	
	public static void delete(DBEntity dbEntity) {
		getMongoDB(dbEntity.getServerId()).remove(dbEntity);
	}
}












