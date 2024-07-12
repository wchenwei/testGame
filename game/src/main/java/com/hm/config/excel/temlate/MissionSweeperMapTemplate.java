package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_sweeper_map")
public class MissionSweeperMapTemplate {
	private Integer id;
	private String map;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
}
