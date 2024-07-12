package com.hm.redis.util;

import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.redis.type.RedisConstants;
import redis.clients.jedis.Jedis;
public class PlayerItemsRedisUtil {
	public static void updatePlayerItemRedis(BasePlayer player,Items items) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Threshold);
		jedis.zadd(items.getItemType()+"_"+items.getId(), (double)items.getCount(), player.getId()+"");
		RedisDbUtil.returnResource(jedis);
	}
	
}
