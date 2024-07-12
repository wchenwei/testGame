package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_101_shop")
public class Active101ShopTemplate {
	private Integer id;
	private Integer level_low;
	private Integer level_high;
	private String goods;
	private String price;
	private Integer limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel_low() {
		return level_low;
	}

	public void setLevel_low(Integer level_low) {
		this.level_low = level_low;
	}
	public Integer getLevel_high() {
		return level_high;
	}

	public void setLevel_high(Integer level_high) {
		this.level_high = level_high;
	}
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
