package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("military_project_level")
public class MilitaryProjectLevelTemplate {
	private Integer level;
	private Integer exp_lv;
	private Integer exp_total;
	private String attr;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getExp_lv() {
		return exp_lv;
	}

	public void setExp_lv(Integer exp_lv) {
		this.exp_lv = exp_lv;
	}
	public Integer getExp_total() {
		return exp_total;
	}

	public void setExp_total(Integer exp_total) {
		this.exp_total = exp_total;
	}
	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}
}
