package com.hm.redis.tank;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.redis.RedisKeyUtils;
import com.hm.rediscenter.MongoRedisUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayerTankBind {
	private String id;
	private Map<Integer, Integer> tankMap = new HashMap<>();
	
	public PlayerTankBind(Player player) {
		this.id = player.getId()+"";
		this.tankMap = player.playerTank().getTankList().stream()
		.filter(e -> e.getSSSStrangeTankId() > 0).collect(Collectors.toMap(Tank::getId, Tank::getSSSStrangeTankId));
	}
	
	public List<Integer> getBeBindTankList() {
		return Lists.newArrayList(tankMap.values());
	}
	
	public int getBindTankId(int srcTankId) {
		return tankMap.getOrDefault(srcTankId, 0);
	}
	
	
	/**
	 * 保存sss坦克绑定数据
	 */
	public void saveRedis() {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			if(CollUtil.isEmpty(tankMap)) {
				jedis.hdel(buildKeyName(), getId());
			}else{
				String json = GSONUtils.ToJSONString(this);
				jedis.hset(buildKeyName(), getId(), json);
			}
		}
	}
	
	public static PlayerTankBind getPlayerTankBind(Object playerId) {
		try (Jedis jedis = MongoRedisUtils.getJedis()){
			String json = jedis.hget(buildKeyName(), playerId+"");
			if(StrUtil.isEmpty(json)) {
				return null;
			}
			return GSONUtils.FromJSONString(json, PlayerTankBind.class);
		}
	}
	public static Map<Integer,Integer> getPlayerTankBindMap(Object playerId) {
		PlayerTankBind tankBind = getPlayerTankBind(playerId);
		return tankBind != null?tankBind.getTankMap():Maps.newHashMap();
	}
	
	
	public static String buildKeyName() {
		return RedisKeyUtils.buildKeyName("_bindtank");
	}
}
