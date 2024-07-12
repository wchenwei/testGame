package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_peace_shop")
public class KfPeaceShopTemplate {
	private Integer id;
	private Integer type_sub;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private Integer location;
	private Integer recharge_gift;
	private String price;
	private String reward;
	private Integer limit;
	private Float discount;
	private Integer price_base;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType_sub() {
		return type_sub;
	}

	public void setType_sub(Integer type_sub) {
		this.type_sub = type_sub;
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
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public Integer getPrice_base() {
		return price_base;
	}

	public void setPrice_base(Integer price_base) {
		this.price_base = price_base;
	}
}
