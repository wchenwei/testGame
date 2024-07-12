package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionSweeperMapTemplate;

import java.util.Map;

@FileConfig("mission_sweeper_map")
public class RareTreasureMapTemplate extends MissionSweeperMapTemplate{
	private Map<Integer,Integer> pointMap = Maps.newConcurrentMap();

	public void init(){
		for(String str:this.getMap().split(",")){
			int point = Integer.parseInt(str.split(":")[1]);
			int eventType = Integer.parseInt(str.split(":")[0]);
			this.pointMap.put(point, eventType);
		}
	}
	
	public Map<Integer,Integer> getPointMap(){
		return pointMap;
	}
}
