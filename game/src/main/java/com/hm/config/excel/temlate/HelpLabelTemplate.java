package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("help_label")
public class HelpLabelTemplate {
	private Integer id;
	private Integer type;
	private String type_txt;

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
	public String getType_txt() {
		return type_txt;
	}

	public void setType_txt(String type_txt) {
		this.type_txt = type_txt;
	}
}
