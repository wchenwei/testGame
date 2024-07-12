package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("vice_soldier_circle")
public class ViceSoldierCircleTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private Integer request_case;
	private String attr;

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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getRequest_case() {
		return request_case;
	}

	public void setRequest_case(Integer request_case) {
		this.request_case = request_case;
	}
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
}
