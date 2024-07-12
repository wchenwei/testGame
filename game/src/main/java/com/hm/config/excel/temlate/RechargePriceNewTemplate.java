package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("recharge_price_new")
public class RechargePriceNewTemplate {
	private Integer id;
	private Integer price;
	private Integer vip_point;
	private String name;
	private String sever_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
}
