package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_lantern_random_shop")
public class ActiveLanternRandomShopTemplate {
	private Integer id;
	private Integer server_level_low;
	private Integer server_level_high;
	private Integer type;
	private String goods;
	private String price;
	private Integer buy_type;
	private Integer limit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getServer_level_low() {
		return server_level_low;
	}

	public void setServer_level_low(Integer server_level_low) {
		this.server_level_low = server_level_low;
	}
	public Integer getServer_level_high() {
		return server_level_high;
	}

	public void setServer_level_high(Integer server_level_high) {
		this.server_level_high = server_level_high;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	public Integer getBuy_type() {
		return buy_type;
	}

	public void setBuy_type(Integer buy_type) {
		this.buy_type = buy_type;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
