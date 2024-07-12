package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("grow_buff_display")
public class GrowBuffDisplayTemplate {
	private Integer id;
	private String icon;
	private String buff_name;
	private String buff_desc;

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
	public String getBuff_name() {
		return buff_name;
	}

	public void setBuff_name(String buff_name) {
		this.buff_name = buff_name;
	}
	public String getBuff_desc() {
		return buff_desc;
	}

	public void setBuff_desc(String buff_desc) {
		this.buff_desc = buff_desc;
	}
}
