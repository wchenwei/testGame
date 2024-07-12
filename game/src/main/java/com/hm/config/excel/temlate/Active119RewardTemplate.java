package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_119_reward")
public class Active119RewardTemplate {
	private Integer id;
	private Integer type;
	private Integer stage;
	private Integer pos;
	private Integer rare;
	private Integer reward_id;
	private Integer rate;
	private String main_reward;
	private String Unlock_Position;

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
	public Integer getReward_id() {
		return reward_id;
	}

	public void setReward_id(Integer reward_id) {
		this.reward_id = reward_id;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public String getMain_reward() {
		return main_reward;
	}

	public void setMain_reward(String main_reward) {
		this.main_reward = main_reward;
	}
	public String getUnlock_Position() {
		return Unlock_Position;
	}

	public void setUnlock_Position(String Unlock_Position) {
		this.Unlock_Position = Unlock_Position;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
}
