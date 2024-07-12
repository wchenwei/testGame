package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WarCraftTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("war_craft")
public class WarCraftTemplateImpl extends WarCraftTemplate{
	private List<Items> costs = Lists.newArrayList();

	private TankAttr attr = new TankAttr(); 
	
	public void init(){
		this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		Map<TankAttrType, Double> attrMap = TankAttrUtils.str2TankAttrMap(this.getAttri(), ",", ":");
		attr.addAttr(attrMap);
	}
	
	public List<Items> getCosts() {
		return costs;
	}
	public TankAttr getAttr() {
		return attr;
	}
}
