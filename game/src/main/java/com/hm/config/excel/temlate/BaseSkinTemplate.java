package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("base_skin")
public class BaseSkinTemplate {
	private Integer base_lv1;
	private Integer base_lv2;
	private Integer id;

	public Integer getBase_lv1() {
		return base_lv1;
	}

	public void setBase_lv1(Integer base_lv1) {
		this.base_lv1 = base_lv1;
	}
	public Integer getBase_lv2() {
		return base_lv2;
	}

	public void setBase_lv2(Integer base_lv2) {
		this.base_lv2 = base_lv2;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
