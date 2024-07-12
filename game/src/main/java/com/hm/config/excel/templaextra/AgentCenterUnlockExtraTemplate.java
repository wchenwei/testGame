package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.AgentCenterUnlockTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("agent_center_unlock")
public class AgentCenterUnlockExtraTemplate extends AgentCenterUnlockTemplate {
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();

	public void init(){
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}
	
	public Map<TankAttrType,Double> getAttrMap(){
		return attrMap;
	}
}
