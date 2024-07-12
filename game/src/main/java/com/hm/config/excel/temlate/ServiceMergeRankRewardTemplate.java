package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("service_merge_rank_reward")
public class ServiceMergeRankRewardTemplate {
	private Integer id;
	private Integer service_level_lower;
	private Integer service_level_upper;
	private Integer type;
	private Integer rank1;
	private Integer rank2;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getService_level_lower() {
		return service_level_lower;
	}

	public void setService_level_lower(Integer service_level_lower) {
		this.service_level_lower = service_level_lower;
	}
	public Integer getService_level_upper() {
		return service_level_upper;
	}

	public void setService_level_upper(Integer service_level_upper) {
		this.service_level_upper = service_level_upper;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getRank1() {
		return rank1;
	}

	public void setRank1(Integer rank1) {
		this.rank1 = rank1;
	}
	public Integer getRank2() {
		return rank2;
	}

	public void setRank2(Integer rank2) {
		this.rank2 = rank2;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
