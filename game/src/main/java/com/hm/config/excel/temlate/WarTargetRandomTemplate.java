package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_target_random")
public class WarTargetRandomTemplate {
	private Integer id;
	private Integer rate;
	private Integer rate_bottom;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Integer getRate_bottom() {
		return rate_bottom;
	}

	public void setRate_bottom(Integer rate_bottom) {
		this.rate_bottom = rate_bottom;
	}
}
