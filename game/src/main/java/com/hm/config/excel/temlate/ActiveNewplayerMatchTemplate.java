package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_newplayer_match")
public class ActiveNewplayerMatchTemplate {
	private Integer id;
	private Integer rank_type;
	private Integer stage;
	private Integer rank_down;
	private Integer rank_up;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRank_type() {
		return rank_type;
	}

	public void setRank_type(Integer rank_type) {
		this.rank_type = rank_type;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
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
