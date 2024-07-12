package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0909_recharge_back")
public class Active0909RechargeBackTemplate {
	private Integer id;
	private Integer stage;
	private Integer id_sub;
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
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getId_sub() {
		return id_sub;
	}

	public void setId_sub(Integer id_sub) {
		this.id_sub = id_sub;
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
