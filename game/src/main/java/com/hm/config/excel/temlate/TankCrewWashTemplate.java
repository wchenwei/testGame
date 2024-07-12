package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew_wash")
public class TankCrewWashTemplate {
	private Integer id;
	private String cost;
	private String attri_library;
	private String blank_library;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getAttri_library() {
		return attri_library;
	}

	public void setAttri_library(String attri_library) {
		this.attri_library = attri_library;
	}
	public String getBlank_library() {
		return blank_library;
	}

	public void setBlank_library(String blank_library) {
		this.blank_library = blank_library;
	}
}
