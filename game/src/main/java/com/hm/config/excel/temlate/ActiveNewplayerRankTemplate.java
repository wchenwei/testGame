package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_newplayer_rank")
public class ActiveNewplayerRankTemplate {
	private Integer id;
	private Integer rank_type;
	private Integer rank_num;
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
	public Integer getRank_num() {
		return rank_num;
	}

	public void setRank_num(Integer rank_num) {
		this.rank_num = rank_num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
