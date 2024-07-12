package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankCrewWashTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("tank_crew_wash")
public class TankPassengerWashTemplate extends TankCrewWashTemplate{
	private RandomRatio washRatio;
	private RandomRatio washRatio2;
	private List<Items> costs = Lists.newArrayList();
	public void init() {
		this.washRatio = new RandomRatio(this.getAttri_library());
		this.washRatio2 = new RandomRatio(this.getBlank_library());
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
	}
	//获取随机到的技能
	public int getWash(){
		return washRatio.randomRatio();
	}
	//获取随机到的技能(有可能为空)
	public int getWash2(){
		return washRatio2.randomRatio();
	}
	
	public List<Items> getCosts(){
		return costs;
	}
}
