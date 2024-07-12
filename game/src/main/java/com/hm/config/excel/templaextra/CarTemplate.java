package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CarBaseTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.Map;

@FileConfig("car_base")
public class CarTemplate extends CarBaseTemplate{
	private Items costItems = new Items();
	
	public void init(){
		this.costItems = ItemUtils.str2Item(getCost(), ":");
	}

	public Items getCostItems() {
		return costItems.clone();
	}
	
	public Map<TankAttrType, Double> getAttrMap(int stage){
		Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
		attrMap.put(TankAttrType.ATK, (double)(getAtk()+stage*getAtk_add()));
		attrMap.put(TankAttrType.DEF, (double)(getDef()+stage*getDef_add()));
		attrMap.put(TankAttrType.HP, (double)(getHp()+stage*getHp_add()));
		return attrMap;
	}

}
