package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("power_rate")
public class PowerRateTemplate {
	private Integer attr_id;
	private Integer rate;

	public Integer getAttr_id() {
		return attr_id;
	}

	public void setAttr_id(Integer attr_id) {
		this.attr_id = attr_id;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
}
