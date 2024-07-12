package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_manor_fight_city")
public class KfManorFightCityTemplate {
	private Integer id;
	private String name;
	private Integer location;
	private String link_city;
	private Integer score_speed;
	private Integer city_terrain;

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
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public String getLink_city() {
		return link_city;
	}

	public void setLink_city(String link_city) {
		this.link_city = link_city;
	}
	public Integer getScore_speed() {
		return score_speed;
	}

	public void setScore_speed(Integer score_speed) {
		this.score_speed = score_speed;
	}
	
	public Integer getCity_terrain() {
		return city_terrain;
	}

	public void setCity_terrain(Integer city_terrain) {
		this.city_terrain = city_terrain;
	}
}
