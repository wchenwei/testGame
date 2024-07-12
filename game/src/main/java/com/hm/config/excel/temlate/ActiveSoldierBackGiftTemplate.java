package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_soldier_back_gift")
public class ActiveSoldierBackGiftTemplate {
	private Integer id;
	private String gift_name;
	private String icon;
	private Integer quality;
	private Integer price_base;
	private Integer buy_times;
	private Integer open_level;
	private Integer recharge_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getGift_name() {
		return gift_name;
	}

	public void setGift_name(String gift_name) {
		this.gift_name = gift_name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getPrice_base() {
		return price_base;
	}

	public void setPrice_base(Integer price_base) {
		this.price_base = price_base;
	}
	public Integer getBuy_times() {
		return buy_times;
	}

	public void setBuy_times(Integer buy_times) {
		this.buy_times = buy_times;
	}
	public Integer getOpen_level() {
		return open_level;
	}

	public void setOpen_level(Integer open_level) {
		this.open_level = open_level;
	}
	public Integer getRecharge_id() {
		return recharge_id;
	}

	public void setRecharge_id(Integer recharge_id) {
		this.recharge_id = recharge_id;
	}
}
