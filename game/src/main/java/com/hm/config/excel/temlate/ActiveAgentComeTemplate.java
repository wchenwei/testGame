package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_agent_come")
public class ActiveAgentComeTemplate {
	private Integer id;
	private Integer stage;
	private Integer pos;
	private Integer rare;
	private String reward;
	private Integer rate;
	private Integer lucky_rate;
	private String main_reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
	public Integer getRare() {
		return rare;
	}

	public void setRare(Integer rare) {
		this.rare = rare;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Integer getLucky_rate() {
		return lucky_rate;
	}

	public void setLucky_rate(Integer lucky_rate) {
		this.lucky_rate = lucky_rate;
	}
	public String getMain_reward() {
		return main_reward;
	}

	public void setMain_reward(String main_reward) {
		this.main_reward = main_reward;
	}
}
