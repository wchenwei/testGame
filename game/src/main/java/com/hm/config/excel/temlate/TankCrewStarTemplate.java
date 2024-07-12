package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew_star")
public class TankCrewStarTemplate {
	private Integer id;
	private Integer quality;
	private Integer star_level;
	private Float buff;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getStar_level() {
		return star_level;
	}

	public void setStar_level(Integer star_level) {
		this.star_level = star_level;
	}
	public Float getBuff() {
		return buff;
	}

	public void setBuff(Float buff) {
		this.buff = buff;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
