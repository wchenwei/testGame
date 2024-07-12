package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("agent_feedback")
public class AgentFeedbackTemplate {
	private Integer id;
	private Integer intimacy;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIntimacy() {
		return intimacy;
	}

	public void setIntimacy(Integer intimacy) {
		this.intimacy = intimacy;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
