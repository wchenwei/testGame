package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("military_lineup_type")
public class MilitaryLineupTypeTemplate {
	private Integer id;
	private String formation;
	private String name;
	private String icon;
	private Integer lv_unclock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFormation() {
		return formation;
	}

	public void setFormation(String formation) {
		this.formation = formation;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getLv_unclock() {
		return lv_unclock;
	}

	public void setLv_unclock(Integer lv_unclock) {
		this.lv_unclock = lv_unclock;
	}
}
