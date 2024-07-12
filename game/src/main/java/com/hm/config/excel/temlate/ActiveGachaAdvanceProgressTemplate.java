package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_gacha_advance_progress")
public class ActiveGachaAdvanceProgressTemplate {
	private Integer id;
	private Integer ground;
	private Integer num;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGround() {
		return ground;
	}

	public void setGround(Integer ground) {
		this.ground = ground;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
