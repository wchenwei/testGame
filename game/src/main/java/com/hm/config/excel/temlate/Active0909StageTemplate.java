package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0909_stage")
public class Active0909StageTemplate {
	private Integer id;
	private String cost_attack_boss;
	private Integer base_cost_gold;
	private String reward_cost_gold;
	private Integer reward_resource_army;
	private String icon_resource;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCost_attack_boss() {
		return cost_attack_boss;
	}

	public void setCost_attack_boss(String cost_attack_boss) {
		this.cost_attack_boss = cost_attack_boss;
	}
	public Integer getBase_cost_gold() {
		return base_cost_gold;
	}

	public void setBase_cost_gold(Integer base_cost_gold) {
		this.base_cost_gold = base_cost_gold;
	}
	public String getReward_cost_gold() {
		return reward_cost_gold;
	}

	public void setReward_cost_gold(String reward_cost_gold) {
		this.reward_cost_gold = reward_cost_gold;
	}
	public Integer getReward_resource_army() {
		return reward_resource_army;
	}

	public void setReward_resource_army(Integer reward_resource_army) {
		this.reward_resource_army = reward_resource_army;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
}
