package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.SuperWeaponUpgradeTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

@FileConfig("super_weapon_upgrade")
public class SuperWeaponUpgradeExtrTemplate extends SuperWeaponUpgradeTemplate{
	private List<Items> costs = Lists.newArrayList();
	private Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
	
	public void init(){
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		if(StringUtils.isNotBlank(getAttr())){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttr(), ",", ":");
		}
	}

	public List<Items> getCosts() {
		return costs;
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
}
