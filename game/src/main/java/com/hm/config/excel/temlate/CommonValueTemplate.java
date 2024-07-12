package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("common_value")
public class CommonValueTemplate {
	private Integer id;
	private Float value;
	private String para;
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
