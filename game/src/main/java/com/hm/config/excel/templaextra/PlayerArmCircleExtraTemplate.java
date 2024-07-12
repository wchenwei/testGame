package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerArmCircleTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("player_arm_circle")
public class PlayerArmCircleExtraTemplate extends PlayerArmCircleTemplate{
	private Map<TankAttrType,Double> attrMap = Maps.newConcurrentMap();
	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttr(), ",", ":");
	}
	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
}
