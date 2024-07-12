package com.hm.redis.troop;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.rediscenter.MongoRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * @Description: 玩家tank部队信息,用于处理sss坦克绑定奇兵
 * @author siyunlong  
 * @date 2020年7月2日 下午4:58:02 
 * @version V1.0
 */
@Slf4j
public class TroopRedisUtils {

	/**
	 * 检查tank是否出现在其他部队系统里
	 * @param playerId
	 * @param tankId
	 * @return
	 */
	public static List<Integer> checkPlayerTank(long playerId,int tankId) {
		List<Integer> typeList = Lists.newArrayList();
		for (TroopRedisType type : TroopRedisType.values()) {
			TroopRedis troopRedis = getTroopRedis(playerId, type);
			if(troopRedis != null && troopRedis.isContainsTank(tankId)) {
				typeList.add(type.getType());
			}
		}
		return typeList;
	}
	
	public static Map<Integer,List<Integer>> checkPlayerTank(long playerId,List<Integer> tankIds) {
		Map<Integer,List<Integer>> map = Maps.newConcurrentMap();
		tankIds.forEach(t->{
			map.put(t, checkPlayerTank(playerId,t));
		});
		return map;
	}
	
	public static void doMergeServerForPlayer(List<String> deletePlayerId) {
		try {
			doMergeServerForPlayer(TroopRedisType.World, deletePlayerId);
			doMergeServerForPlayer(TroopRedisType.ArenaPrimary, deletePlayerId);
			doMergeServerForPlayer(TroopRedisType.ArenaMedium, deletePlayerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 和服是删除玩家的部队系统
	 * @param player
	 */
	public static void doMergeServerForPlayer(TroopRedisType type,List<String> deletePlayerId) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String key = TroopRedis.buildKeyName(type);
			final int size = deletePlayerId.size();
            final int pageSize = 200;
            for (int i = 0; i < size; i += pageSize) {
                List<String> page = deletePlayerId.subList(i, Math.min(size, i + pageSize));
                Long num = jedis.hdel(key, page.toArray(new String[0]));
                log.error("delete " + num + " troopRedis from redis");
            }
		}
	}
	
	
	/**
	 * 获取玩家某个系统的部队tank信息
	 * @param playerId
	 * @param type
	 * @return
	 */
	public static TroopRedis getTroopRedis(long playerId,TroopRedisType type) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String key = TroopRedis.buildKeyName(type);
			String json = jedis.hget(key, playerId+"");
			if(StrUtil.isEmpty(json)) {
				return null;
			}
			return GSONUtils.FromJSONString(json, TroopRedis.class);
		}
	}
	
	/**
	 * 删除某个系统的部队数据
	 * @param type
	 */
	public static void dropColl(TroopRedisType type) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String key = TroopRedis.buildKeyName(type);
			jedis.del(key);
		} 
	}
	/**
	 * 删除某个系统玩家的部队数据
	 * @param type
	 * @param playerId
	 */
	public static void delKey(TroopRedisType type,long playerId) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String key = TroopRedis.buildKeyName(type);
			jedis.hdel(key, playerId+"");
		}
	}
	
	public static void main(String[] args) {
		long playerId = 600306;
		System.err.println(checkPlayerTank(playerId, 1));
		dropColl(TroopRedisType.KFManor);
	}
}
