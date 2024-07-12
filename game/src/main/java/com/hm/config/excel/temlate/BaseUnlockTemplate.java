package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("base_unlock")
public class BaseUnlockTemplate {
	private Integer base_level;
	private Integer building_id;

	public Integer getBase_level() {
		return base_level;
	}

	public void setBase_level(Integer base_level) {
		this.base_level = base_level;
	}
	public Integer getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Integer building_id) {
		this.building_id = building_id;
	}
}
