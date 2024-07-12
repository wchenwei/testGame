package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sea_trade_road")
public class SeaTradeRoadTemplate {
	private Integer id;
	private Integer start_city;
	private Integer end_city;
	private Integer longth;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStart_city() {
		return start_city;
	}

	public void setStart_city(Integer start_city) {
		this.start_city = start_city;
	}
	public Integer getEnd_city() {
		return end_city;
	}

	public void setEnd_city(Integer end_city) {
		this.end_city = end_city;
	}
	public Integer getLongth() {
		return longth;
	}

	public void setLongth(Integer longth) {
		this.longth = longth;
	}
}
