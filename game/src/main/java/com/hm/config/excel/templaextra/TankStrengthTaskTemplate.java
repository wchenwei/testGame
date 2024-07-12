package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.BuildingTaskTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("building_task")
public class TankStrengthTaskTemplate extends BuildingTaskTemplate{
	private int strengthNum;
	private int needNum;
	private Map<TankAttrType, Double> attrMap = Maps.newHashMap();
	
	public void init() {
		String[] finish = this.getTask_finish().split(":");
		this.strengthNum = Integer.parseInt(finish[1]);
		this.needNum = Integer.parseInt(finish[0]);
		this.attrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
	}

	public int getStrengthNum() {
		return strengthNum;
	}

	public int getNeedNum() {
		return needNum;
	}

	public Map<TankAttrType, Double> getAttrMap() {
		return attrMap;
	}
	
}
