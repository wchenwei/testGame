package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("recharge_price")
public class RechargePriceTemplate {
	private Integer id;
	private Integer type;
	private Integer diamonds;
	private Integer else_diamonds;
	private Integer price;
	private Integer vip_point;
	private Integer days;
	private Integer month_gift;
	private Integer specail_gift;
	private String name;
	private String sever_name;
	private Integer index;
	private Integer first_double;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(Integer diamonds) {
		this.diamonds = diamonds;
	}
	public Integer getElse_diamonds() {
		return else_diamonds;
	}

	public void setElse_diamonds(Integer else_diamonds) {
		this.else_diamonds = else_diamonds;
	}
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getVip_point() {
		return vip_point;
	}

	public void setVip_point(Integer vip_point) {
		this.vip_point = vip_point;
	}
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getMonth_gift() {
		return month_gift;
	}

	public void setMonth_gift(Integer month_gift) {
		this.month_gift = month_gift;
	}
	public Integer getSpecail_gift() {
		return specail_gift;
	}

	public void setSpecail_gift(Integer specail_gift) {
		this.specail_gift = specail_gift;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSever_name() {
		return sever_name;
	}

	public void setSever_name(String sever_name) {
		this.sever_name = sever_name;
	}
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getFirst_double() {
		return first_double;
	}

	public void setFirst_double(Integer first_double) {
		this.first_double = first_double;
	}
}
