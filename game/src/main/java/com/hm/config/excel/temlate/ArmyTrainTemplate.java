package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("army_train")
public class ArmyTrainTemplate {
	private Integer num;
	private String cost;
	private Integer limit_add;
	private String cost_time_add;
	private Integer time_add;
	private String cost_materials_room;
	private String reward_tank;
	private String reward_rocket;
	private String reward_armor;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getLimit_add() {
		return limit_add;
	}

	public void setLimit_add(Integer limit_add) {
		this.limit_add = limit_add;
	}
	public String getCost_time_add() {
		return cost_time_add;
	}

	public void setCost_time_add(String cost_time_add) {
		this.cost_time_add = cost_time_add;
	}
	public Integer getTime_add() {
		return time_add;
	}

	public void setTime_add(Integer time_add) {
		this.time_add = time_add;
	}
	public String getCost_materials_room() {
		return cost_materials_room;
	}

	public void setCost_materials_room(String cost_materials_room) {
		this.cost_materials_room = cost_materials_room;
	}
	public String getReward_tank() {
		return reward_tank;
	}

	public void setReward_tank(String reward_tank) {
		this.reward_tank = reward_tank;
	}
	public String getReward_rocket() {
		return reward_rocket;
	}

	public void setReward_rocket(String reward_rocket) {
		this.reward_rocket = reward_rocket;
	}
	public String getReward_armor() {
		return reward_armor;
	}

	public void setReward_armor(String reward_armor) {
		this.reward_armor = reward_armor;
	}
}
