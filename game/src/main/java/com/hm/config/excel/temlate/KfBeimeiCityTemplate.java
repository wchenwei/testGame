package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_beimei_city")
public class KfBeimeiCityTemplate {
	private Integer id;
	private Integer belong;
	private Integer fog;
	private Integer main_city;
	private Integer air_defense;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBelong() {
		return belong;
	}

	public void setBelong(Integer belong) {
		this.belong = belong;
	}
	public Integer getFog() {
		return fog;
	}

	public void setFog(Integer fog) {
		this.fog = fog;
	}
	public Integer getMain_city() {
		return main_city;
	}

	public void setMain_city(Integer main_city) {
		this.main_city = main_city;
	}
	public Integer getAir_defense() {
		return air_defense;
	}

	public void setAir_defense(Integer air_defense) {
		this.air_defense = air_defense;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
