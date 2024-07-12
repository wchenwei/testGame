package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_SScar_shop")
public class ActiveSscarShopTemplate {
	private Integer id;
	private Integer stage;
	private Integer pos;
	private Integer reward_id;
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
	public Integer getReward_id() {
		return reward_id;
	}

	public void setReward_id(Integer reward_id) {
		this.reward_id = reward_id;
	}
	public String getMain_reward() {
		return main_reward;
	}

	public void setMain_reward(String main_reward) {
		this.main_reward = main_reward;
	}
}
