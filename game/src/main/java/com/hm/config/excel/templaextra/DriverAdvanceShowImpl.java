package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.DriverAdvanceShowTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("driver_advance_show")
public class DriverAdvanceShowImpl extends DriverAdvanceShowTemplate {
	Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	
	public void init(){
		if(!getAttri_choose().isEmpty()){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri_choose(), ":");
		}
	}
	public Map<TankAttrType, Double> getAttr(){
		return attrMap; 
	}
}
