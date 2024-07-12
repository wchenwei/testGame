package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("shop_vip")
public class ShopVipTemplate {
	private Integer lv_vip;
	private Integer exp_vip;
	private String vip_power;
	private String vip_power_show;
	private Integer gift;
	private Integer free_gift;
	private String gift_price;
	private String free_gift_price;

	public Integer getLv_vip() {
		return lv_vip;
	}

	public void setLv_vip(Integer lv_vip) {
		this.lv_vip = lv_vip;
	}
	public Integer getExp_vip() {
		return exp_vip;
	}

	public void setExp_vip(Integer exp_vip) {
		this.exp_vip = exp_vip;
	}
	public String getVip_power() {
		return vip_power;
	}

	public void setVip_power(String vip_power) {
		this.vip_power = vip_power;
	}
	public String getVip_power_show() {
		return vip_power_show;
	}

	public void setVip_power_show(String vip_power_show) {
		this.vip_power_show = vip_power_show;
	}
	public Integer getGift() {
		return gift;
	}

	public void setGift(Integer gift) {
		this.gift = gift;
	}
	public Integer getFree_gift() {
		return free_gift;
	}

	public void setFree_gift(Integer free_gift) {
		this.free_gift = free_gift;
	}
	public String getGift_price() {
		return gift_price;
	}

	public void setGift_price(String gift_price) {
		this.gift_price = gift_price;
	}
	public String getFree_gift_price() {
		return free_gift_price;
	}

	public void setFree_gift_price(String free_gift_price) {
		this.free_gift_price = free_gift_price;
	}
}
