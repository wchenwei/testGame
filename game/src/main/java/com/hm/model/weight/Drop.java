package com.hm.model.weight;

import com.google.common.collect.Maps;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;


public class Drop {
	private WeightRandom<Items> weightRandom;
	private int itemSize;
	
	
	public Drop() {
		super();
	}
	
	public Drop(String drop) {
		super();
		this.init(drop);
	}

	public void init(String drop) {
		if(StringUtils.isNotBlank(drop)){
			Map<Items,Integer> weightMap = Maps.newHashMap();
			String[] rewardList = drop.split(",");
			for (String rewardStr : rewardList) {
				String[] rewards = rewardStr.split(":");
				int type = Integer.parseInt(rewards[0]);
				int id = Integer.parseInt(rewards[1]);
				int num = Integer.parseInt(rewards[2]);
				int weight = Integer.parseInt(rewards[3]);
				Items item = new Items(id);
				item.setItemType(type);
				item.setCount(num);
				weightMap.put(item, weight);
				itemSize ++;
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}
	
	public int getItemSize() {
		return itemSize;
	}

	public Items randomItem() {
		if(weightRandom != null) {
			return weightRandom.next().clone();
		}
		return null;
	}
}
