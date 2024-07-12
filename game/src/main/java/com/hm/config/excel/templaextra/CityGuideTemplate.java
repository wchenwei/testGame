package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.GuideBookTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
@FileConfig("guide_book")
public class CityGuideTemplate extends GuideBookTemplate {
	private List<Items> rewards = Lists.newArrayList();
	private int rewardCityId;
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		List<Integer> cityIds = StringUtil.splitStr2IntegerList(this.getTarget_city(), ",");
		//取最后一个为奖励城市
		this.rewardCityId = cityIds.get(cityIds.size()-1);
	}

	public List<Items> getRewards() {
		return rewards;
	}

	public int getRewardCityId() {
		return rewardCityId;
	}
	
	
	

}
