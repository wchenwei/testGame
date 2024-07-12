package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("city_war_skill")
public class CityWarSkillTemplate {
	private Integer id;
	private String name;
	private Integer unlock_city;
	private String use_condition;
	private String dec;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getUnlock_city() {
		return unlock_city;
	}

	public void setUnlock_city(Integer unlock_city) {
		this.unlock_city = unlock_city;
	}
	public String getUse_condition() {
		return use_condition;
	}

	public void setUse_condition(String use_condition) {
		this.use_condition = use_condition;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
}
