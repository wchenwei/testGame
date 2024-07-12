package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BuildingTrainingGroundTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("building_training_ground")
public class TankTrainingGroundTemplate extends BuildingTrainingGroundTemplate{
	private List<Items> buffCosts = Lists.newArrayList();
	private List<Items> trainCosts = Lists.newArrayList();
	private Items reviveCost;

	@ConfigInit
	public void init() {
		buffCosts = ItemUtils.str2ItemList(this.getCost_buff(), ",", ":");
		trainCosts = ItemUtils.str2ItemList(this.getCost_fight(), ",", ":");
		this.reviveCost = ItemUtils.str2Item(this.getCost_revive(), ":");
	}

	public List<Items> getBuffCosts() {
		return buffCosts;
	}

	public List<Items> getTrainCosts() {
		return trainCosts;
	}

	public Items getReviveCost() {
		return reviveCost.clone();
	}
	
	
	
}
