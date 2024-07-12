package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("military_lineup_level")
public class MilitaryLineupLevelTemplate {
	private Integer id;
	private Integer type;
	private Integer mi_level;
	private String cost;
	private Float attri;

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
	public Integer getMi_level() {
		return mi_level;
	}

	public void setMi_level(Integer mi_level) {
		this.mi_level = mi_level;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Float getAttri() {
		return attri;
	}

	public void setAttri(Float attri) {
		this.attri = attri;
	}
}
