package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("service_merge_recharge_reward")
public class ServiceMergeRechargeRewardTemplate {
	private Integer id;
	private Integer service_level_lower;
	private Integer service_level_upper;
	private Integer recharge_num;
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
	public Integer getRecharge_num() {
		return recharge_num;
	}

	public void setRecharge_num(Integer recharge_num) {
		this.recharge_num = recharge_num;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
