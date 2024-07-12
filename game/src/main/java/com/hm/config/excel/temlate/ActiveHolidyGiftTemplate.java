package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_holidy_gift")
public class ActiveHolidyGiftTemplate {
	private Integer id;
	private String gift_name;
	private String icon;
	private Integer quality;
	private Integer price_base;
	private Integer buy_times;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer recharge_id;
	private Integer equip_quality;

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
	public Integer getRecharge_id() {
		return recharge_id;
	}

	public void setRecharge_id(Integer recharge_id) {
		this.recharge_id = recharge_id;
	}
	public Integer getEquip_quality() {
		return equip_quality;
	}

	public void setEquip_quality(Integer equip_quality) {
		this.equip_quality = equip_quality;
	}
}
