package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("total_war_match")
public class TotalWarMatchTemplate {
	private Integer id;
	private String range;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
}
