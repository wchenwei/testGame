package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_119_gift")
public class Active119GiftTemplate {
	private Integer id;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer location;
	private Integer quality;
	private String name;
	private String gift_Icon;
	private Integer price_base;
	private Float discount;
	private Integer buy_times;
	private Integer recharge_gift_id;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlayer_lv_down() {
		return player_lv_down;
	}

	public void setPlayer_lv_down(Integer player_lv_down) {
		this.player_lv_down = player_lv_down;
	}
	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getGift_Icon() {
		return gift_Icon;
	}

	public void setGift_Icon(String gift_Icon) {
		this.gift_Icon = gift_Icon;
	}
	public Integer getPrice_base() {
		return price_base;
	}

	public void setPrice_base(Integer price_base) {
		this.price_base = price_base;
	}
	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public Integer getBuy_times() {
		return buy_times;
	}

	public void setBuy_times(Integer buy_times) {
		this.buy_times = buy_times;
	}
	public Integer getRecharge_gift_id() {
		return recharge_gift_id;
	}

	public void setRecharge_gift_id(Integer recharge_gift_id) {
		this.recharge_gift_id = recharge_gift_id;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
