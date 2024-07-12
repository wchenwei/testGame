package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.SuperWeaponTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("super_weapon")
public class SuperWeaponExTemplate extends SuperWeaponTemplate{
	private List<Items> costItems = Lists.newArrayList(); 
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	
	public void init(){
		this.costItems = ItemUtils.str2ItemList(getCost(), ",", ":");
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttr(), ",", ":");
	}

	public List<Items> getCostItems() {
		return costItems;
	}
	
	public Map<TankAttrType, Double> getAttrMap(){
		return attrMap;
	}

}
