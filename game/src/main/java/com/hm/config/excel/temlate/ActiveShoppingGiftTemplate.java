package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_shopping_gift")
public class ActiveShoppingGiftTemplate {
	private Integer id;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private Integer days;
	private Integer location;
	private Integer recharge_gift;
	private String reward;
	private Integer limit_total;
	private Integer limit_single;
	private Integer type;
	private String price;
	private Integer limit_consume_gold;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServer_lv_down() {
		return server_lv_down;
	}

	public void setServer_lv_down(Integer server_lv_down) {
		this.server_lv_down = server_lv_down;
	}
	public Integer getServer_lv_up() {
		return server_lv_up;
	}

	public void setServer_lv_up(Integer server_lv_up) {
		this.server_lv_up = server_lv_up;
	}
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getRecharge_gift() {
		return recharge_gift;
	}

	public void setRecharge_gift(Integer recharge_gift) {
		this.recharge_gift = recharge_gift;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getLimit_total() {
		return limit_total;
	}

	public void setLimit_total(Integer limit_total) {
		this.limit_total = limit_total;
	}
	public Integer getLimit_single() {
		return limit_single;
	}

	public void setLimit_single(Integer limit_single) {
		this.limit_single = limit_single;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getLimit_consume_gold() {
		return limit_consume_gold;
	}

	public void setLimit_consume_gold(Integer limit_consume_gold) {
		this.limit_consume_gold = limit_consume_gold;
	}
}
