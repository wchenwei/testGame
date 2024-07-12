package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.FetterCircleTemplate;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("fetter_circle")
public class MemorialBuffTemplate extends FetterCircleTemplate{
	private Map<Integer,Double> attrMap = Maps.newConcurrentMap();
	public void init() {
		this.attrMap = TankAttrUtils.str2TankAttrIntMap(getAttri(), ",", ":");
	}
	
	public Map<Integer, Double> getAttrMap() {
		return Maps.newHashMap(attrMap);
	}
	
}
