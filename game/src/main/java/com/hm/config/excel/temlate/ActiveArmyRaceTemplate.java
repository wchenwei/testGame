package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_army_race")
public class ActiveArmyRaceTemplate {
	private Integer id;
	private Integer type;
	private Integer star;
	private Integer reward_group;
	private Integer army_power;

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
	public Integer getStar() {
		return star;
	}

	public void setReward_group(Integer reward_group) {
		this.reward_group = reward_group;
	}
	public Integer getArmy_power() {
		return army_power;
	}

	public void setArmy_power(Integer army_power) {
		this.army_power = army_power;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getReward_group() {
		return reward_group;
	}
}
