package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_enhance")
public class BuildingEnhanceTemplate {
	private Integer id;
	private Integer item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}
}
