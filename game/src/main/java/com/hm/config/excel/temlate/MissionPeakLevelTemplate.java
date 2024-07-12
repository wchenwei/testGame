package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_peak_level")
public class MissionPeakLevelTemplate {
	private Integer id;
	private String first_reward;
	private String reward;
	private Integer cost;
	private Float attri_add;
	private String library;
	private String rare;
	private Integer next_lv;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirst_reward() {
		return first_reward;
	}

	public void setFirst_reward(String first_reward) {
		this.first_reward = first_reward;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Float getAttri_add() {
		return attri_add;
	}

	public void setAttri_add(Float attri_add) {
		this.attri_add = attri_add;
	}
	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}
	public String getRare() {
		return rare;
	}

	public void setRare(String rare) {
		this.rare = rare;
	}
	public Integer getNext_lv() {
		return next_lv;
	}

	public void setNext_lv(Integer next_lv) {
		this.next_lv = next_lv;
	}
}
