package com.hm.rediscenter;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.CentreArms;
import com.hm.servercontainer.centreArms.CentreArmsContainer;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeInfo;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Description: 原本放在mongo里的 移到redis存储
 * @author siyunlong  
 * @date 2020年4月11日 下午8:07:04 
 * @version V1.0
 */
public class MongoRedisUtils {

	
	public static String buildKeyName(String collName,int serverId) {
		return MongoUtils.getGameDBName()+"_"+collName+"_"+serverId;
	}
	
	public static Jedis getJedis() {
		return CenterRedisUtils.getMongoPool().getResource();
	}

	
	public static <T extends BaseEntityMapper> void megrge(int serverId, List<Integer> newServerIdsList) {
		MongoRedisUtils.megrge(CentreArms.class, serverId, newServerIdsList);
		MongoRedisUtils.megrge(IdCodeInfo.class,serverId,newServerIdsList);
		CentreArmsContainer.of(serverId).initContainer();
		IdCodeContainer.of(serverId).initContainer();
	}

	public static <T extends BaseEntityMapper> void megrge(Class<T> entityClass, int serverId,
			List<Integer> newServerIdsList) {
		for(int sid:newServerIdsList){
			List<T> dataList = RedisMapperUtil.queryListAll(sid, entityClass);
			dataList.forEach(t ->t.setServerId(serverId));
			RedisMapperUtil.updateAll(serverId, dataList, entityClass);
		}
	}
}
