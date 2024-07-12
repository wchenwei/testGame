package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerArmTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("player_arm")
public class PlayerArmExtraTemplate extends PlayerArmTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	private List<Items> costs = Lists.newArrayList();
	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttr(), ",", ":");
		this.costs = ItemUtils.str2ItemList(this.getUpgrade_cost(), ",", ":");
	}
	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	public List<Items> getCosts() {
		return costs;
	}
	
	
	
	
}
