package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CarDriverUpgradeTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("car_driver_upgrade")
public class CarDriverUpgradeExtraTemplate extends CarDriverUpgradeTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	private List<Items> costs = Lists.newArrayList();

	public void init(){
		if(StrUtil.isNotBlank(getAttri())) {
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
		}
		if(StrUtil.isNotBlank(this.getCost())) {
			this.costs = ItemUtils.str2ItemList(this.getCost(), ",", ":");
		}
	}
	
	public Map<TankAttrType,Double> getAttrMap(){
		return attrMap;
	}

	public List<Items> getCosts() {
		return costs;
	}
	
}
