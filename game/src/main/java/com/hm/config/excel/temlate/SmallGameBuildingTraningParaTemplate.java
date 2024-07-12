package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("small_game_building_traning_para")
public class SmallGameBuildingTraningParaTemplate {
	private String key;
	private Integer value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
