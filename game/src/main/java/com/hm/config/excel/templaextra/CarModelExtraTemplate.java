package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CarModelTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("car_model")
public class CarModelExtraTemplate extends CarModelTemplate{
	private List<List<Items>> costs = Lists.newArrayList();
	private List<Map<TankAttrType, Double>> attrMaps = Lists.newArrayList(); 
	
	public void init(){
		String[] unlockStrs = getUnlock().split(";");
		for(String str:unlockStrs) {
			costs.add(ItemUtils.str2ItemList(str, ",",":"));
		}
		String[] attrs = getAttr().split(";");
		for(String str:attrs) {
			attrMaps.add(TankAttrUtils.str2TankAttrMap(str, ",", ":"));
		}
	}

	public List<Items> getCostItems(int star) {
		return costs.get(star);
	}
	
	public Map<TankAttrType, Double> getAttrMap(int star){
		return attrMaps.get(star);
	}
	
	public int getMaxStar() {
		return attrMaps.size()-1;
	}

	public int getPaperId(){
		List<Items> items = this.costs.stream().filter(e -> CollUtil.isNotEmpty(e)).findFirst().orElse(null);
		Items first = CollUtil.getFirst(items);
		return first == null? 0: first.getId();
	}
}
