package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("total_war_reward")
public class TotalWarRewardTemplate {
	private Integer id;
	private Integer type;
	private Integer number;
	private String reward;
	private String turn_card;

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
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getTurn_card() {
		return turn_card;
	}

	public void setTurn_card(String turn_card) {
		this.turn_card = turn_card;
	}
}
