package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankCrewWashAttriTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("tank_crew_wash_attri")
public class TankPassengerWashAttriTemplate extends TankCrewWashAttriTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}
	public Map<TankAttrType, Double> getAttrMap(){
		return attrMap;
	}
	
	public Map<TankAttrType, Double> getAttrMap(double rate){
		Map<TankAttrType, Double> map = Maps.newConcurrentMap();
		attrMap.forEach((key,value)->{
			map.put(key, value*(1+rate));
		});
		return map;
	}
}
