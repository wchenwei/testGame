package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_title")
public class PlayerTitleTemplate {
	private Integer id;
	private String url;
	private String dec;
	private Integer type;
	private String icon_s;
	private String icon_l;
	private Integer last_time;
	private Integer order;
	private String attr;
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon_s() {
		return icon_s;
	}

	public void setIcon_s(String icon_s) {
		this.icon_s = icon_s;
	}
	public String getIcon_l() {
		return icon_l;
	}

	public void setIcon_l(String icon_l) {
		this.icon_l = icon_l;
	}
	public Integer getLast_time() {
		return last_time;
	}

	public void setLast_time(Integer last_time) {
		this.last_time = last_time;
	}
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
