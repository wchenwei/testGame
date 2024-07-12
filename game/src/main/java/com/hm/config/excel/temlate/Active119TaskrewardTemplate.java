package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_119_taskreward")
public class Active119TaskrewardTemplate {
	private Integer id;
	private Integer show;
	private String Round;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}
	public String getRound() {
		return Round;
	}

	public void setRound(String Round) {
		this.Round = Round;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
