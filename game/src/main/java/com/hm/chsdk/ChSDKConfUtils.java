package com.hm.chsdk;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 草花服務器sdk配置
 * @author siyunlong  
 * @date 2020年3月16日 下午2:30:32 
 * @version V1.0
 */
public class ChSDKConfUtils {
	public static Map<Integer,ChSDKConf> confMap = Maps.newConcurrentMap();
	
	public static void init() {
		Query query = new Query();
		query.addCriteria(Criteria.where("serverSDK").exists(true));
		List<ChSDKConf> confList = MongoUtils.getLoginMongodDB()
				.query(query, ChSDKConf.class,"channelInfo")
				.stream().filter(e -> StrUtil.isNotEmpty(e.getSdkInfo())).collect(Collectors.toList());
		Map<Integer,ChSDKConf> tempMap = Maps.newHashMap();
		for (ChSDKConf chSDKConf : confList) {
			chSDKConf.init();
			tempMap.put(chSDKConf.getChannelId(), chSDKConf);
		}
		confMap = ImmutableMap.copyOf(tempMap);
	}
	
	public static ChSDKConf getChSDKConf(int channelId) {
		return confMap.get(channelId);
	}
}
