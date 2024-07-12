package com.hm.config.excel.temlate;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;

//掉落配置，可掉落多个，可配置必掉的（必掉的权重，为小于0，）
@FileConfig("drop_base")
public class DropTemplate extends DropBaseTemplate{
	private List<Items> mustDropList = Lists.newArrayList();
	private List<WeightRandom<Items>> weightRandomList;
	
	public void init() {
		weightRandomList = Lists.newArrayList();
		String[] rewardListAll = getReward().split(";");
		for(String rewardStrAll : rewardListAll) {
			String[] rewardList = rewardStrAll.split(",");
			weightRandomList.add(this.getWeightRandom(rewardList));
		}
	}
	
	public List<Items> randomItem() {
		List<Items> itemList = getMustItems();
		if(!CollectionUtil.isEmpty(weightRandomList)) {
			for(WeightRandom<Items> weightRandom: weightRandomList) {
				Items item = weightRandom.next();
				if(item != null && item.getItemType() > 0) {
					itemList.add(item.clone());
				}
			}
		}
		return itemList;
	}
	
	private List<Items> getMustItems() {
		List<Items> itemList = Lists.newArrayList();
		for (Items items : mustDropList) {
			itemList.add(items.clone());
		}
		return itemList;
	}
	
	private WeightRandom<Items> getWeightRandom(String[] rewardList) {
		Map<Items,Integer> weightMap = Maps.newHashMap();
		for (String rewardStr : rewardList) {
			String[] rewards = rewardStr.split(":");
			int type = Integer.parseInt(rewards[0].trim());
			int id = Integer.parseInt(rewards[1]);
			int num = Integer.parseInt(rewards[2]);
			int weight = Integer.parseInt(rewards[3]);
			Items item = new Items(id);
			item.setItemType(type);
			item.setCount(num);
			if(weight < 0) {
				this.mustDropList.add(item);
			}else{
				weightMap.put(item, weight);
			}
		}
		return RandomUtils.buildWeightRandom(weightMap);
	}
}
