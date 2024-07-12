package com.hm.libcore.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Description: 
 * 用之前请先初始化
 * 采用twitter的id生成器
 * 游戏id生成器,dbId为1024一循环，如果dbId超过1024 会有概率和dbId=0 生成的id重复
 * @author siyunlong  
 * @date 2020年5月8日 下午2:33:22 
 * @version V1.0
 */
public class GameIdUtils {
	public static final int MaxVal = 31;
	
	public static Snowflake idSnowflake;
	
	/**
	 * 按照id生成id生成器
	 * @param dbId
	 */
	public static void init(int dbId) {
		idSnowflake = buildSnowflake(dbId);
	}
	
	public static Snowflake buildSnowflake(int id) {
		int[] result = findDbId(id);
		return IdUtil.getSnowflake(result[0], result[1]);
	}
	
	public static int[] findDbId(int id) {
		List<int[]> allList = Lists.newArrayList();
		for (int i = 0; i <= MaxVal; i++) {
			for (int j = 0; j <= MaxVal; j++) {
				allList.add(new int[]{i,j});
			}
		}
		return allList.get(id%allList.size());
	}
	
	public static long nextId() {
		if(idSnowflake == null) {
			init(RandomUtil.randomInt(MaxVal));
		}
		return idSnowflake.nextId();
	}
	public static String nextStrId() {
		return Long.toString(nextId(), 36)+RandomUtil.randomString(5);
	}
	
	public static String nextStrId(int serverId) {
		return serverId+"_"+nextStrId();
	}

}
