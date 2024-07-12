package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_peak_npc")
public class MissionPeakNpcTemplate {
	private Integer id;
	private Integer npc_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNpc_id() {
		return npc_id;
	}

	public void setNpc_id(Integer npc_id) {
		this.npc_id = npc_id;
	}
}
