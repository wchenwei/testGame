package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_boss_reward")
public class WorldBossRewardTemplate {
	private Integer id;
	private Integer boss;
	private Integer rate;
	private Long target;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBoss() {
		return boss;
	}

	public void setBoss(Integer boss) {
		this.boss = boss;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
