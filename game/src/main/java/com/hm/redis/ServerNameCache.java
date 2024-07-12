package com.hm.redis;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务器昵称缓存
 * @author siyun
 *
 */
public class ServerNameCache {
	private static Map<Integer,String> nameMap = Maps.newConcurrentMap();
	public static Map<Integer, NamingServer> titleNameMap = Maps.newConcurrentMap();
	public static long lastFreshTime = 0;

	public static String getServerNameByPlayerId(long playerId) {
		return getServerName(ServerUtils.getCreateServerId(playerId));
	}

	public static String getServerName(int serverId) {
        try {
            reloadTitleName();
            NamingServer namingServer = titleNameMap.get(serverId);
            if (namingServer != null && namingServer.checkTime()) {
                return namingServer.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
		}
		String name = nameMap.get(serverId);
		if(StrUtil.isEmpty(name)) {
			name = RedisUtil.getServerName(serverId);
			nameMap.put(serverId, name);
		}
		return name;
	}
	
	public static Map<Integer,String> getServerName(List<Integer> ids) {
		Map<Integer,String> result = Maps.newHashMap();
		for (int serverId : ids) {
			result.put(serverId, getServerName(serverId));
		}
		return result;
	}

	public static void reloadTitleName() {
		if (System.currentTimeMillis() > lastFreshTime) {
			titleNameMap = NamingServer.queryAll();
			lastFreshTime = DateUtil.beginOfHour(new Date()).getTime() + (90 + RandomUtil.randomInt(10)) * GameConstants.MINUTE;
			System.out.println("冠名: next time:" + lastFreshTime);
		}
	}

	public static Map<Integer, String> getNameMap() {
		return nameMap;
	}


}
