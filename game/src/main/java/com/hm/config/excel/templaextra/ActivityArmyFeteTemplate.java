package com.hm.config.excel.templaextra;


import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveFreeEnergyTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.stream.Collectors;
@FileConfig("active_free_energy")
public class ActivityArmyFeteTemplate extends ActiveFreeEnergyTemplate {
	private int minLv;
	private int maxLv;
	private List<Items> rewards = Lists.newArrayList();
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward_drop(), ",", ":");
		this.minLv = Integer.parseInt(this.getLevel().split(",")[0]);
		this.maxLv = Integer.parseInt(this.getLevel().split(",")[1]);
	}
	public boolean isCanReward(int lv) {
		int nowHour = DateUtil.thisHour(true);
		return nowHour >= getOpen_time() && nowHour < getEnd_time() && lv >= minLv && lv <= maxLv;
	}
	public List<Items> getRewards() {
		return rewards.stream().map(t -> t.clone()).collect(Collectors.toList());
	}
	public int getMinLv() {
		return minLv;
	}
	public int getMaxLv() {
		return maxLv;
	}
	
	
}
