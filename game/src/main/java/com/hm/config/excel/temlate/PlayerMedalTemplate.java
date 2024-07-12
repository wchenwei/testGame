package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_medal")
public class PlayerMedalTemplate {
	private Integer id;
	private Integer type;
	private Integer medal_id;
	private String name;
	private String icon;
	private String reward;
	private String attri;
	private Integer value;
	private String cost;
	private Integer sub_level;
	private Integer skill;

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
	public Integer getMedal_id() {
		return medal_id;
	}

	public void setMedal_id(Integer medal_id) {
		this.medal_id = medal_id;
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
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getSub_level() {
		return sub_level;
	}

	public void setSub_level(Integer sub_level) {
		this.sub_level = sub_level;
	}
	public Integer getSkill() {
		return skill;
	}

	public void setSkill(Integer skill) {
		this.skill = skill;
	}
}
