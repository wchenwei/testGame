package com.hm.mq.redis;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.leaderboards.RankRedisConfig;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

@Biz
public class RankBiz {
    public Map<String, String> scriptMap = Maps.newConcurrentMap();

    public void init() {
        reloadScript();
    }

    public void reloadScript() {
        Map<String, String> scriptMap = Maps.newHashMap();
        scriptMap.putAll(showScripts("scripts:private"));
        scriptMap.putAll(showScripts("scripts:public"));
		/*for (String key : scriptMap.keySet()) {
			System.err.println(key);
			System.err.println(scriptMap.get(key));
		}*/
        this.scriptMap = scriptMap;
    }

    public Map<String, String> showScripts(String name) {
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            Map<String, String> keyMap = jedis.hgetAll(name);
            return keyMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return Maps.newHashMap();
    }

    public String execScript(String scriptName, List<String> args) {
        String scriptDesc = this.scriptMap.get(scriptName);
        if (StrUtil.isEmpty(scriptDesc)) {
            return null;
        }
        if (RedisRankLog.isShowLog) {
            RedisRankLog.showLog(scriptName + "->" + GSONUtils.ToJSONString(args));
        }
        Jedis jedis = null;
        try {
            jedis = RankRedisConfig.getInstance().getRankPool().getResource();
            Object result = jedis.eval(scriptDesc, Lists.newArrayList(), args);
            return result.toString();
        } finally {
            returnResource(jedis);
        }
    }

    public String execScript(String scriptName, Object... args) {
        return execScript(scriptName, ObjectArrayAsStringArray(args));
    }

    public static List<String> ObjectArrayAsStringArray(Object[] objectArray) {
        List<String> results = Lists.newArrayList();
        for (int i = 0; i < objectArray.length; i++) {
            results.add(objectArray[i].toString());
        }
        return results;
    }

    public static void returnResource(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

}
