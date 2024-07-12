package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.NpcRoadLineTemplate;

import java.util.List;

@FileConfig("npc_road_line")
public class RoadLineTemplate extends NpcRoadLineTemplate{
	private List<Integer> wayList = Lists.newArrayList();
	private int start;
	private int end;
	
	public void init() {
		this.wayList = StringUtil.splitStr2IntegerList(getRoad_line(), ",");
		this.start = this.wayList.get(0);
		this.end = this.wayList.get(this.wayList.size()-1);
	}

	public List<Integer> getWayList() {
		return wayList;
	}
	
	public boolean isFit(int start,int end) {
		return start == this.start && end == this.end;
	}
	
	public boolean isFit(int cityId) {
		return this.wayList.contains(cityId);
	}
	
	public int getNextCity(Integer cityId) {
		int index = this.wayList.indexOf(cityId);
		if(index >= 0 && index < this.wayList.size()-1) {
			return this.wayList.get(index+1);
		}
		return -1;
	}
	
}
