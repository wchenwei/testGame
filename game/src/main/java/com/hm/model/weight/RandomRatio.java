package com.hm.model.weight;

import com.google.common.collect.Maps;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.util.RandomUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class RandomRatio {
	private WeightRandom<Integer> weightRandom;
	private int itemSize;
	
	
	public RandomRatio() {
		super();
	}
	
	public RandomRatio(String str) {
		super();
		this.init(str);
	}
	public RandomRatio(String str,List<Integer> filterIds) {
		super();
		this.init(str,filterIds);
	}
	
	public RandomRatio(Map<Integer,Integer> rateMap){
		super();
		this.init(rateMap);
	}

	private void init(Map<Integer, Integer> rateMap) {
		Map<Integer,Integer> weightMap = Maps.newHashMap();
		for(Map.Entry<Integer, Integer> entry: rateMap.entrySet()){
			int num = entry.getKey();
			int weight = entry.getValue();
			weightMap.put(num, weight);
			itemSize ++;
		}
		this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
	}
	public void init(String str,List<Integer> filterIds) {
		if(StringUtils.isNotBlank(str)){
			Map<Integer,Integer> weightMap = Maps.newHashMap();
			String[] str2 = str.split(",");
			for (String rewardStr : str2) {
				String[] rewards = rewardStr.split(":");
				int id = Integer.parseInt(rewards[0]);
				int weight = Integer.parseInt(rewards[1]);//权重
				if(!filterIds.contains(id)){
					weightMap.put(id, weight);
					itemSize ++;
				}
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}

	public void init(String str) {
		if(StringUtils.isNotBlank(str)){
			Map<Integer,Integer> weightMap = Maps.newHashMap();
			String[] str2 = str.split(",");
			for (String rewardStr : str2) {
				String[] rewards = rewardStr.split(":");
				int num = Integer.parseInt(rewards[0]);//倍数
				int weight = Integer.parseInt(rewards[1]);//权重
				weightMap.put(num, weight);
				itemSize ++;
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}
	
	public int getItemSize() {
		return itemSize;
	}

	public int randomRatio() {
		if(weightRandom != null) {
			int result = weightRandom.next();
			return result;
		}
		return 1;
	}
}
