package com.hm.server;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.KfType;
import com.hm.model.activity.kfactivity.KfServerInfo;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.util.RandomUtils;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Data
public class KfUrlCount {
	public KfServerInfo kfInfo;
	public int count;
	
	public KfUrlCount(KfServerInfo kfInfo, int count) {
		super();
		this.kfInfo = kfInfo;
		this.count = count;
	}

	public static KfUrlCount findLuckUrl() {
		List<KfServerInfo> kfList = KfServerInfo.getKfServerInfo(KfType.KfExpedetion.getType());
		Map<String,String> redisMap = RedisTypeEnum.KfExpedtionUrl.getAllVal();
		List<KfUrlCount> urlList = Lists.newArrayList();
		for (KfServerInfo temp : kfList) {
			if(!redisMap.containsKey(temp.getId())) {
				return new KfUrlCount(temp,0);
			}
			int count = Integer.parseInt(redisMap.getOrDefault(temp.getId(), "0"));
			if(temp.getWeight() >= count) {
				urlList.add(new KfUrlCount(temp,count));
			}
		}
		if(CollUtil.isEmpty(urlList)) {
			KfServerInfo luckInfo = randomKfUrlByWeight(kfList);
			return new KfUrlCount(luckInfo, Integer.parseInt(redisMap.getOrDefault(luckInfo.getId(), "0")));
		}
		KfUrlCount result = urlList.stream().min(Comparator.comparing(KfUrlCount::getCount)).orElse(null);
		return result;
	}
	
	public static KfServerInfo randomKfUrlByWeight(List<KfServerInfo> kfList) {
		Map<KfServerInfo,Integer> weightMap = Maps.newHashMap();
		for (KfServerInfo kfServerInfo : kfList) {
			if(kfServerInfo.getWeight() > 0) {
				weightMap.put(kfServerInfo, kfServerInfo.getWeight());
			}
		}
		if(CollUtil.isNotEmpty(weightMap)) {
			return RandomUtils.buildWeightMeta(weightMap).random();
		}
		return RandomUtils.randomEle(kfList);
	}
	
	public void addCount() {
		this.count ++;
		RedisTypeEnum.KfExpedtionUrl.put(kfInfo.getId(), this.count+"");
	}
	
	public static void main(String[] args) {
		RedisTypeEnum.KfExpedtionUrl.dropColl();
//		for (int i = 0; i < 30; i++) {
//			KfUrlCount luck = findLuckUrl();
//			System.err.println(luck.getKfInfo().getId());
//			luck.addCount();
//		}
		System.err.println(RedisTypeEnum.KfExpedtionUrl.getAllVal());
	}
}
