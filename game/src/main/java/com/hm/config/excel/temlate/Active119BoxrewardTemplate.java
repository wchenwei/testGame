package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_119_boxreward")
public class Active119BoxrewardTemplate {
	private Integer id;
	private Integer stage;
	private Integer Round;
	private String Round_reward;

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
	public Integer getRound() {
		return Round;
	}

	public void setRound(Integer Round) {
		this.Round = Round;
	}
	public String getRound_reward() {
		return Round_reward;
	}

	public void setRound_reward(String Round_reward) {
		this.Round_reward = Round_reward;
	}
}
