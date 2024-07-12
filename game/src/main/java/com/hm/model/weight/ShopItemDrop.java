package com.hm.model.weight;

import com.google.common.collect.Maps;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.model.item.Items;
import com.hm.model.shop.ShopItem;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class ShopItemDrop {
	private WeightRandom<ShopItem> weightRandom;
	private int itemSize;
	
	
	public ShopItemDrop() {
		super();
	}
	
	public ShopItemDrop(int id,String drop,String cost) {
		super();
		this.init(id,drop,cost);
	}

	public void init(int id,String drop,String cost) {
		if(StringUtils.isNotBlank(drop)){
			Map<ShopItem,Integer> weightMap = Maps.newHashMap();
			String[] rewardArray = drop.split(",");
			String[] costArray = cost.split(",");
			for(int i=0;i<rewardArray.length;i++){
				int[] rewards = StringUtil.strToIntArray(rewardArray[i], ":");
				int[] costs = StringUtil.strToIntArray(costArray[i], ":");
				Items rewardItem = new Items(rewards[1], rewards[2], rewards[0]);
				int weight = rewards[3];
				Items costItem = new Items(costs[1],costs[2],costs[0]);
				ShopItem shopItem = new ShopItem(id,rewardItem, costItem);
				weightMap.put(shopItem, weight);
				itemSize++;
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}
	
	public int getItemSize() {
		return itemSize;
	}

	public ShopItem randomShopItem() {
		if(weightRandom != null) {
			return weightRandom.next();
		}
		return null;
	}
}
