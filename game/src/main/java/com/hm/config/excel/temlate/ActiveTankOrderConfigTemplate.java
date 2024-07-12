package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_tank_order_config")
public class ActiveTankOrderConfigTemplate {
	private Integer id;
	private String name;
	private String icon;
	private Integer level_range;
	private Integer level_next;
	private Integer refresh_price;
	private String reward;
	private String double_price;
	private Integer limit;

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
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getLevel_range() {
		return level_range;
	}

	public void setLevel_range(Integer level_range) {
		this.level_range = level_range;
	}
	public Integer getLevel_next() {
		return level_next;
	}

	public void setLevel_next(Integer level_next) {
		this.level_next = level_next;
	}
	public Integer getRefresh_price() {
		return refresh_price;
	}

	public void setRefresh_price(Integer refresh_price) {
		this.refresh_price = refresh_price;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getDouble_price() {
		return double_price;
	}

	public void setDouble_price(String double_price) {
		this.double_price = double_price;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
