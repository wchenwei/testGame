package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_boss")
public class WorldBossTemplate {
	private Integer id;
	private Integer level;
	private Long hp;
	private String name;
	private String kill_reward;
	private String reward_show;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getHp() {
		return hp;
	}

	public void setHp(Long hp) {
		this.hp = hp;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getKill_reward() {
		return kill_reward;
	}

	public void setKill_reward(String kill_reward) {
		this.kill_reward = kill_reward;
	}
	public String getReward_show() {
		return reward_show;
	}

	public void setReward_show(String reward_show) {
		this.reward_show = reward_show;
	}
}
