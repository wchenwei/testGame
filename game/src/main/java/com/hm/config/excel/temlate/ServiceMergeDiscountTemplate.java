package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("service_merge_discount")
public class ServiceMergeDiscountTemplate {
	private Integer id;
	private Integer player_level_lower;
	private Integer player_level_upper;
	private String reward;
	private Integer price_now;
	private Integer price;
	private Integer discount;
	private Integer buy_limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlayer_level_lower() {
		return player_level_lower;
	}

	public void setPlayer_level_lower(Integer player_level_lower) {
		this.player_level_lower = player_level_lower;
	}
	public Integer getPlayer_level_upper() {
		return player_level_upper;
	}

	public void setPlayer_level_upper(Integer player_level_upper) {
		this.player_level_upper = player_level_upper;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getPrice_now() {
		return price_now;
	}

	public void setPrice_now(Integer price_now) {
		this.price_now = price_now;
	}
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getBuy_limit() {
		return buy_limit;
	}

	public void setBuy_limit(Integer buy_limit) {
		this.buy_limit = buy_limit;
	}
}
