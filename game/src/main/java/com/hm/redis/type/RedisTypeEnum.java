package com.hm.redis.type;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.util.RedisDbUtil;
import com.hm.util.StringUtil;
import lombok.val;
import redis.clients.jedis.Jedis;

import java.util.*;
//存放数据。无序的数据
public enum RedisTypeEnum {
	
	Player("player","玩家"),
	Guild("guild","部落信息"),
	InviteCode("inviteCode","邀请码信息"),
	BindPhone("bindPhone","绑定手机号"),
	KfResMine("KfResMine","跨服资源"),
	KfBuildExtort("KfBuildExtort","跨服建筑征讨"),
	
	PlayerAddr("PlayerAddr","玩家地址"),
	EggTrueItems("EggTrueItems","砸蛋实物"),
	Activity11Charge("Activity11","双11计费购买"),
	RechargeCarnivalAddr("RechargeCarnivalAddr","充值狂欢收货地址"),
	ServerName("ServerName","服务器对应名称"),
	Activity12Charge("Activity12","双12计费购买"),
	ActivityDiscount111("Discount111","限时抢购（1.11)"),
	KFPlayerLevelRankNum("kfPlayerLevelRankNum","玩家段位赛次数"),
	InviteInfo("InviteInfo","玩家邀请信息"),
	AnniversaryAddr("AnniversaryAddr","充值狂欢收货地址"),
	KfExpedtionUrl("KfExpedtionUrl","跨服远征地址"),
	KfResPlayerLv("KfResPlayerLv","跨服资源战进阶等级"),
	Act97Recharge("Act97Recharge","6周年跨服活动排行榜"),
	Act121Recharge("Act121Recharge","新版国庆跨服活动排行榜"),
	PlayerAircraftFormation("PlayerAircraftFormation","玩家航母飞机阵型"),
	KFSeasonTopLog("KFSeasonTopLog","赛季排名信息"),
	CampOfficial("CampOfficial", "阵营官员"),
    PlayerReword("playerreword", "老用户回流奖励"),
    BadWord("badWord", "关键字信息"),
    KFTankLotteryLucKPlayers("KFTankLotteryLucKPlayers", "彩票获奖玩家ids"),
	Act81BulletScreen("Act81BulletScreen", "81 活动跨服弹幕"),

	RemovePlayer("RemovePlayer", "用户注销"),
	FriendGift("FriendGift", "玩家赠送礼物"),
	Account2Id("Account2Id", "账号最后一次登录的玩家id"),
	ServerData("ServerData", "服务器数据"),
	LoginPlayerData("LoginPlayerData", "玩家数据"),

	GameInnerIp("GameInnerIp", "外网ip对应的内网ip"),

	PlayerLastLoginTime("PlayerLastLoginTime", "玩家最后一次登录时间"),
	;
	
	private RedisTypeEnum(String key,String desc){
		this.key = key;
		this.desc = desc;
	}
	private String key;
	private String desc;
	
	//增加到redis中，1表示新增，0表示更新
	//此方法是用于存储带serverid区分的hash，
	public long put(int serverId, String field, String value) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hset(this.getId(serverId), field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return 0;
	}

	//此方法是存储不区分server的数据。
	public long put(String field, String value) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hset(this.getKey(), field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return 0;
	}

	public void putObject(String field, Object object) {
		String value = isPrimitive(object) ? object.toString() : GSONUtils.ToJSONString(object);
		put(field, value);
	}


	public long put(Object field, String value) {
		return this.put(String.valueOf(field), value);
	}
	public long put(Object field, int value) {
		return this.put(String.valueOf(field), String.valueOf(value));
	}
	
	//从redis获取数据，带serverId区分的
	public String get(int serverId, String field) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hget(this.getId(serverId), field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			RedisDbUtil.returnResource(jedis);
		}
		return null;
	}
	//从redis后去数据，不带serverid区分的
	public String get(String field) {
		Jedis jedis = null;
		try {
			jedis = RedisDbUtil.getJedis(RedisConstants.Player);
			return jedis.hget(this.getKey(), field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return null;
	}
	
	public boolean isExit(String field){
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hexists(this.getKey(), field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return false;
	}
	
	public Set<String> getAllKeys() {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hkeys(this.getKey());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return Sets.newHashSet();
	}
	
	public Map<String,String> getAllVal() {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hgetAll(this.getKey());
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}

	public Map<String,String> getAllVal(int serverId) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hgetAll(this.getId(serverId));
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}
	public String getStr(int field) {
		return this.get(String.valueOf(field));
	}
	public int getInt(int field) {
		return Integer.parseInt(this.get(String.valueOf(field)));
	}
	
	//从redis获取数据，1表示删除成功，0表示没有此field值
	public long del(int serverId, String field) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hdel(this.getId(serverId), field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			RedisDbUtil.returnResource(jedis);
		}
		return 0;
	}
	public long del(String field) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hdel(this.getKey(), field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			RedisDbUtil.returnResource(jedis);
		}
		return 0;
	}
	public void dropColl() {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			jedis.del(this.getKey());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			RedisDbUtil.returnResource(jedis);
		}
	}
	
	public long del(Number field) {
		return this.del(String.valueOf(field));
	}
	
	//批量放入数据到redis中
	public void putBatch(int serverId, Map<String, String> data) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			jedis.hmset(getId(serverId), data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}
	public void putBatch(Map<String, String> data) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			jedis.hmset(getKey(), data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}
	
	public List<String> getListObj(RedisTypeEnum redisType, List<Long> ids) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			return jedis.hmget(getKey(), listToStrArrays(ids));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return Lists.newArrayList();
	}
	@SuppressWarnings("unused")
	private String getDesc() {
		return desc;
	}
	public static RedisTypeEnum getRedisType(String type) {
		return Arrays.stream(RedisTypeEnum.values()).filter(t ->StringUtil.equals(t.getKey(), type)).findFirst().orElse(null);
	}
	
	public static String[] listToStrArrays(List ids) {
		String[] keys = new String[ids.size()];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = ids.get(i)+"";
 		}
		return keys;
	}

    public Set<String> getBadWord() {
        Set<String> words = new HashSet<String>();
        Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
        try {
            return jedis.hgetAll(this.getKey()).keySet();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisDbUtil.returnResource(jedis);
        }
        return words;
    }

	//此方法是存储不区分server的数据。
	public long rename() {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			String toKey = String.format("%s_%s_%s", this.getKey(), DateUtil.format(new Date(), "yyyyMMdd"), "back");
			return jedis.renamenx(this.getKey(), toKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return 0;
	}

	public void delKey(int serverId) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			jedis.del(this.getId(serverId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}

	public void lpush(int serverId, String value) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		try {
			jedis.lpush(this.getId(serverId), value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
	}

	public List<String> lrange(int serverId, int start, int end) {
		Jedis jedis = RedisDbUtil.getJedis(RedisConstants.Player);
		List<String> list = Lists.newArrayList();
		try {
			list = jedis.lrange(this.getId(serverId), start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisDbUtil.returnResource(jedis);
		}
		return list;
	}

	public String getKey() {
		return MongoUtils.getGameDBName() + "_" + key;
	}

	private String getId(int serverId) {
		return String.format("%s_%s", this.getKey(), serverId);
	}

	/**
	 * 判断一个对象是否是基本类型或基本类型的封装类型
	 */
	public static boolean isPrimitive(Object obj) {
		try {
			if (obj == null) {
				return false;
			}
			if (obj instanceof String) {
				return true;
			}
			return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
}










