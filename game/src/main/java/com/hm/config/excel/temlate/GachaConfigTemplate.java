package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("gacha_config")
public class GachaConfigTemplate {
	private Integer id;
	private Integer type;
	private String cost_item;
	private Integer cost_gold;
	private String reward;
	private String reward_gettank;
	private Integer else_reward;
	private Integer free_time;
	private String gift_reward;

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
	public String getCost_item() {
		return cost_item;
	}

	public void setCost_item(String cost_item) {
		this.cost_item = cost_item;
	}
	public Integer getCost_gold() {
		return cost_gold;
	}

	public void setCost_gold(Integer cost_gold) {
		this.cost_gold = cost_gold;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getReward_gettank() {
		return reward_gettank;
	}

	public void setReward_gettank(String reward_gettank) {
		this.reward_gettank = reward_gettank;
	}
	public Integer getElse_reward() {
		return else_reward;
	}

	public void setElse_reward(Integer else_reward) {
		this.else_reward = else_reward;
	}
	public Integer getFree_time() {
		return free_time;
	}

	public void setFree_time(Integer free_time) {
		this.free_time = free_time;
	}
	public String getGift_reward() {
		return gift_reward;
	}

	public void setGift_reward(String gift_reward) {
		this.gift_reward = gift_reward;
	}
}
