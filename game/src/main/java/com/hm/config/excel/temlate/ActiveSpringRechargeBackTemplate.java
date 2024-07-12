package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_spring_recharge_back")
public class ActiveSpringRechargeBackTemplate {
	private Integer id;
	private Integer charge_gold;
	private Integer limit;
	private String feed_back;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCharge_gold() {
		return charge_gold;
	}

	public void setCharge_gold(Integer charge_gold) {
		this.charge_gold = charge_gold;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getFeed_back() {
		return feed_back;
	}

	public void setFeed_back(String feed_back) {
		this.feed_back = feed_back;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
