package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("npc_road_line")
public class NpcRoadLineTemplate {
	private Integer id;
	private String road_line;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoad_line() {
		return road_line;
	}

	public void setRoad_line(String road_line) {
		this.road_line = road_line;
	}
}
