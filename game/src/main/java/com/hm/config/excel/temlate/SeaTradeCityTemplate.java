package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sea_trade_city")
public class SeaTradeCityTemplate {
	private Integer id;
	private String name;
	private String link_city;
	private String sell_item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getLink_city() {
		return link_city;
	}

	public void setLink_city(String link_city) {
		this.link_city = link_city;
	}
	public String getSell_item() {
		return sell_item;
	}

	public void setSell_item(String sell_item) {
		this.sell_item = sell_item;
	}
}
