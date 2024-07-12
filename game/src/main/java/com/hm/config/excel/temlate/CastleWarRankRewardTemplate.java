package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("castle_war_rank_reward")
public class CastleWarRankRewardTemplate {
	private Integer id;
	private Integer castal_type;
	private Integer rank_1;
	private Integer rank_2;
	private String reward_list;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCastal_type() {
		return castal_type;
	}

	public void setCastal_type(Integer castal_type) {
		this.castal_type = castal_type;
	}
	public Integer getRank_1() {
		return rank_1;
	}

	public void setRank_1(Integer rank_1) {
		this.rank_1 = rank_1;
	}
	public Integer getRank_2() {
		return rank_2;
	}

	public void setRank_2(Integer rank_2) {
		this.rank_2 = rank_2;
	}
	public String getReward_list() {
		return reward_list;
	}

	public void setReward_list(String reward_list) {
		this.reward_list = reward_list;
	}
}
