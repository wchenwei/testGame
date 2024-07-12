package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankCrewSuitAttriTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

@FileConfig("tank_crew_suit_attri")
public class TankPassengerSuitAttriTemplate extends TankCrewSuitAttriTemplate{
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap(); 
	public void init(){
		if(StringUtils.isNotBlank(getAttri())){
			this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
		}
	}
	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
}
