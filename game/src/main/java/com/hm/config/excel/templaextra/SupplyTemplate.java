package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ExpressTaskTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@FileConfig("express_task")
public class SupplyTemplate extends ExpressTaskTemplate{
	private Items items;
	private WeightMeta<SupplyTaskSetting> weightMeta;
	private WeightMeta<SupplyTaskSetting> npcMeta;
	
	public void init() {
		this.items = ItemUtils.str2Item(getReward_item(), ":");
		this.weightMeta = buildMeta(100);
		this.npcMeta = buildMeta(3);
	}
	
	private WeightMeta<SupplyTaskSetting> buildMeta(int maxStart) {
		Map<SupplyTaskSetting,Integer> randomMap = 
				Arrays.stream(getStar_info().split(",")).map(e -> new SupplyTaskSetting(e))
				.filter(e -> e.getStar() < maxStart)
		.collect(Collectors.toMap(Function.identity(), SupplyTaskSetting::getWeight));
		if(randomMap.isEmpty()) {
			return null;
		}
		randomMap.keySet().forEach(e -> e.setItem(this.items));
		randomMap.keySet().forEach(e -> e.buildItems());
		return RandomUtils.buildWeightMeta(randomMap);
	}
	
	public SupplyTaskSetting randomSupplyTaskSetting() {
		return this.weightMeta.random();
	}
	
	public SupplyTaskSetting randomNpcTaskSetting() {
		return this.npcMeta.random();
	}
	
	public boolean isFitNpc() {
		return this.npcMeta != null;
	}
	
	public boolean isFit(int lv) {
		return lv >= getLevel();
	}
}
