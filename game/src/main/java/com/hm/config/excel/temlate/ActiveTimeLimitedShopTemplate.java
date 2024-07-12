package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_time_limited_shop")
public class ActiveTimeLimitedShopTemplate {
	private Integer id;
	private Integer level_low;
	private Integer level_high;
	private String goods;
	private String price;
	private String price_base;
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
	public String getPrice_base() {
		return price_base;
	}

	public void setPrice_base(String price_base) {
		this.price_base = price_base;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
