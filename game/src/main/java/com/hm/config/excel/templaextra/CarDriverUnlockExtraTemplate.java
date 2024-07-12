package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CarDriverUnlockTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("car_driver_unlock")
public class CarDriverUnlockExtraTemplate extends CarDriverUnlockTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();

	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}
	
	public Map<TankAttrType,Double> getAttrMap(){
		return attrMap;
	}
}
