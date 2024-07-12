package com.hm.model.camp;

import com.google.common.collect.Lists;
import com.hm.model.item.Items;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ExtraCityReward {
	private List<Items> itemList = Lists.newArrayList();//额外的产出
	private int[] cityNum;//每个类型城市的数量
	
	public ExtraCityReward(List<Items> itemList, int[] cityNum) {
		this.itemList = itemList;
		this.cityNum = cityNum;
	}
	
	public List<Items> getItemList() {
		return itemList;
	}

}
