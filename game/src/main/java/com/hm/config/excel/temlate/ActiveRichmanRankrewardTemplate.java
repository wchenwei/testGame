package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_richman_rankreward")
public class ActiveRichmanRankrewardTemplate {
	private Integer index;
	private Integer stage;
	private Integer server_lv_down;
	private Integer server_lv_up;
	private Integer rank_down;
	private Integer rank_up;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getServer_lv_down() {
		return server_lv_down;
	}

	public void setServer_lv_down(Integer server_lv_down) {
		this.server_lv_down = server_lv_down;
	}
	public Integer getServer_lv_up() {
		return server_lv_up;
	}

	public void setServer_lv_up(Integer server_lv_up) {
		this.server_lv_up = server_lv_up;
	}
	public Integer getRank_down() {
		return rank_down;
	}

	public void setRank_down(Integer rank_down) {
		this.rank_down = rank_down;
	}
	public Integer getRank_up() {
		return rank_up;
	}

	public void setRank_up(Integer rank_up) {
		this.rank_up = rank_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
