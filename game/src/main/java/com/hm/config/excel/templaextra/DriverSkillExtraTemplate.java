package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DriverSkillTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("driver_skill")
public class DriverSkillExtraTemplate extends DriverSkillTemplate{
	
	Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	
	public void init(){
		if(!getAttribute().isEmpty()){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttribute(), ":");
		}
	}
	public Map<TankAttrType, Double> getSkillAttr(int lv){
		Map<TankAttrType, Double> skillAttrMap = Maps.newHashMap(); 
		if(lv>0){
			for(Map.Entry<TankAttrType, Double> entry:this.attrMap.entrySet()){
				skillAttrMap.put(entry.getKey(), entry.getValue()+(lv-1)*getGrow());
			}
		}
		return skillAttrMap; 
	}
	
}
