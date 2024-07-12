package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.MilitaryTemplate;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import java.util.Map;

@FileConfig("military")
public class MilitaryExtraTemplate extends MilitaryTemplate {	
	private Items costItems = new Items();

	public void init() {
		this.costItems = ItemUtils.str2Item(getCost(), ":");
	}
	public Items getCostItems() {
		return costItems.clone();
	}


	public Map<TankAttrType, Double> getAttrMap(int stage) {
		Map<TankAttrType, Double> attrMap = Maps.newHashMap();
		attrMap.put(TankAttrType.ATK, (double) (getAtk() + stage * getAtk_add()));
		attrMap.put(TankAttrType.DEF, (double) (getDef() + stage  * getDef_add()));
		attrMap.put(TankAttrType.HP, (double) (getHp() + stage * getHp_add()));
		attrMap.put(TankAttrType.HpPer, (double)getHp_buff());
		return attrMap;
	}

	public double getStifleVal() {
		return MathUtils.div(getSpskill_num(),10000,2);
	}
}
