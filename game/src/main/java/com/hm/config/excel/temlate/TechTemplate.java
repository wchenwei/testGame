package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tech")
public class TechTemplate {
	private String index;
	private Integer tech_type;
	private Integer tech_level;
	private Integer time;
	private Long gold;
	private Long fe;
	private Long oil;
	private Long elec;
	private Long ura;
	private Float value;
	private Integer tech_power;
	private Integer tech_tab;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	public Integer getTech_type() {
		return tech_type;
	}

	public void setTech_type(Integer tech_type) {
		this.tech_type = tech_type;
	}
	public Integer getTech_level() {
		return tech_level;
	}

	public void setTech_level(Integer tech_level) {
		this.tech_level = tech_level;
	}
	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	public Long getGold() {
		return gold;
	}

	public void setGold(Long gold) {
		this.gold = gold;
	}
	public Long getFe() {
		return fe;
	}

	public void setFe(Long fe) {
		this.fe = fe;
	}
	public Long getOil() {
		return oil;
	}

	public void setOil(Long oil) {
		this.oil = oil;
	}
	public Long getElec() {
		return elec;
	}

	public void setElec(Long elec) {
		this.elec = elec;
	}
	public Long getUra() {
		return ura;
	}

	public void setUra(Long ura) {
		this.ura = ura;
	}
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	public Integer getTech_power() {
		return tech_power;
	}

	public void setTech_power(Integer tech_power) {
		this.tech_power = tech_power;
	}
	public Integer getTech_tab() {
		return tech_tab;
	}

	public void setTech_tab(Integer tech_tab) {
		this.tech_tab = tech_tab;
	}
}
