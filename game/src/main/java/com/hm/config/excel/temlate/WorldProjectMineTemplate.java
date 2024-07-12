package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_project_mine")
public class WorldProjectMineTemplate {
	private Integer id;
	private String pos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}
}
