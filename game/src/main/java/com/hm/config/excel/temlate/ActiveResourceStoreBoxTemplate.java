package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_resource_store_box")
public class ActiveResourceStoreBoxTemplate {
	private Integer box_id;
	private String key;
	private String price;
	private Integer buy_limit;
	private String dec;

	public Integer getBox_id() {
		return box_id;
	}

	public void setBox_id(Integer box_id) {
		this.box_id = box_id;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getBuy_limit() {
		return buy_limit;
	}

	public void setBuy_limit(Integer buy_limit) {
		this.buy_limit = buy_limit;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
}
