package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_771_resource")
public class Active771ResourceTemplate {
	private Integer stage;
	private String shop_pic;
	private String name_icon;
	private String active_icon;

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public String getShop_pic() {
		return shop_pic;
	}

	public void setShop_pic(String shop_pic) {
		this.shop_pic = shop_pic;
	}
	public String getName_icon() {
		return name_icon;
	}

	public void setName_icon(String name_icon) {
		this.name_icon = name_icon;
	}
	public String getActive_icon() {
		return active_icon;
	}

	public void setActive_icon(String active_icon) {
		this.active_icon = active_icon;
	}
}
