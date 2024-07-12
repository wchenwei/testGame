package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("shop_item")
public class ShopItemTemplate {
	private Integer index;
	private Integer shop_id;
	private Integer position;
	private String level_zone;
	private String goods;
	private String rate;
	private Integer discount;
	private Integer day_num;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getLevel_zone() {
		return level_zone;
	}

	public void setLevel_zone(String level_zone) {
		this.level_zone = level_zone;
	}
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getDay_num() {
		return day_num;
	}

	public void setDay_num(Integer day_num) {
		this.day_num = day_num;
	}
}
