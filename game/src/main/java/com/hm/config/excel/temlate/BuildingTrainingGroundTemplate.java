package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_training_ground")
public class BuildingTrainingGroundTemplate {
	private Integer id;
	private String name;
	private String unlock;
	private String npc;
	private String npc_onekey;
	private String reward_show;
	private String cost_buff;
	private String cost_fight;
	private String cost_revive;
	private String map;

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
	public String getUnlock() {
		return unlock;
	}

	public void setUnlock(String unlock) {
		this.unlock = unlock;
	}
	public String getNpc() {
		return npc;
	}

	public void setNpc(String npc) {
		this.npc = npc;
	}
	public String getNpc_onekey() {
		return npc_onekey;
	}

	public void setNpc_onekey(String npc_onekey) {
		this.npc_onekey = npc_onekey;
	}
	public String getReward_show() {
		return reward_show;
	}

	public void setReward_show(String reward_show) {
		this.reward_show = reward_show;
	}
	public String getCost_buff() {
		return cost_buff;
	}

	public void setCost_buff(String cost_buff) {
		this.cost_buff = cost_buff;
	}
	public String getCost_fight() {
		return cost_fight;
	}

	public void setCost_fight(String cost_fight) {
		this.cost_fight = cost_fight;
	}
	public String getCost_revive() {
		return cost_revive;
	}

	public void setCost_revive(String cost_revive) {
		this.cost_revive = cost_revive;
	}
	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
}
