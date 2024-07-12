package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.TrumpVsTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;

import java.util.List;

@FileConfig("trump_vs")
public class TrumpTemplate extends TrumpVsTemplate{
	private List<Items> rewards = Lists.newArrayList();
	private List<Integer> tankIds = Lists.newArrayList();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.tankIds = StringUtil.splitStr2IntegerList(getEnemy_tank(), ",");
	}

	public List<Items> getRewards() {
		return Lists.newArrayList(rewards);
	}
	
	public int randomTankIds(List<Integer> filterTankIds) {
		List<Integer> tempList = Lists.newArrayList(tankIds);
		tempList.removeAll(filterTankIds);
		if(CollUtil.isEmpty(tempList)) {
			return RandomUtils.randomEle(tankIds);
		}
		return RandomUtils.randomEle(tempList);
	}
}
