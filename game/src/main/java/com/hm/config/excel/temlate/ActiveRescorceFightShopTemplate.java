package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_rescorce_fight_shop")
public class ActiveRescorceFightShopTemplate {
	private Integer id;
	private String item;
	private Integer cost;
	private Integer buy_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Integer buy_time) {
		this.buy_time = buy_time;
	}
}
