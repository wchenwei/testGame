package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BattleplaneStarTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("battleplane_star")
public class BattleplaneStarTemplateImpl extends BattleplaneStarTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newConcurrentMap();
	public void init() {
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}
	
	public Map<TankAttrType, Double> getAttrMap(){
		return attrMap;
	}
}
