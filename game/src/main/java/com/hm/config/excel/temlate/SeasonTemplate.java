package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("season")
public class SeasonTemplate {
	private Integer id;
	private String icon;
	private String name;
	private String dec;
	private String buff_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getBuff_add() {
		return buff_add;
	}

	public void setBuff_add(String buff_add) {
		this.buff_add = buff_add;
	}
}
