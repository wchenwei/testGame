package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("shop_fortess")
public class ShopFortessTemplate {
	private Integer id;
	private String sale;
	private String cost;
	private Integer limit;
	private Integer effect_show;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getEffect_show() {
		return effect_show;
	}

	public void setEffect_show(Integer effect_show) {
		this.effect_show = effect_show;
	}
}
