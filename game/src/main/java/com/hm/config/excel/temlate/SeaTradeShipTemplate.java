package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("sea_trade_ship")
public class SeaTradeShipTemplate {
	private Integer level;
	private String icon;
	private Integer store;
	private Float speed;
	private Integer reform_cost;
	private Integer reform_time;
	private Float product_speed;
	private Integer gold_time;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}
	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Integer getReform_cost() {
		return reform_cost;
	}

	public void setReform_cost(Integer reform_cost) {
		this.reform_cost = reform_cost;
	}
	public Integer getReform_time() {
		return reform_time;
	}

	public void setReform_time(Integer reform_time) {
		this.reform_time = reform_time;
	}
	public Float getProduct_speed() {
		return product_speed;
	}

	public void setProduct_speed(Float product_speed) {
		this.product_speed = product_speed;
	}

	public Integer getGold_time() {
		return gold_time;
	}

	public void setGold_time(Integer gold_time) {
		this.gold_time = gold_time;
	}
}
