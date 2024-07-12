package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("gift")
public class GiftTemplate {
	private Integer gift_id;
	private String drop_type;
	private String desc;

	public Integer getGift_id() {
		return gift_id;
	}

	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}
	public String getDrop_type() {
		return drop_type;
	}

	public void setDrop_type(String drop_type) {
		this.drop_type = drop_type;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
