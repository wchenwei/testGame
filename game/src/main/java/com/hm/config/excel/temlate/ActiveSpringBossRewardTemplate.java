package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_spring_boss_reward")
public class ActiveSpringBossRewardTemplate {
	private Integer id;
	private Integer boss_lv;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private String reward_random;
	private String reward_final;
	private String reward_show;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBoss_lv() {
		return boss_lv;
	}

	public void setBoss_lv(Integer boss_lv) {
		this.boss_lv = boss_lv;
	}
	public Integer getPlayer_lv_down() {
		return player_lv_down;
	}

	public void setPlayer_lv_down(Integer player_lv_down) {
		this.player_lv_down = player_lv_down;
	}
	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
	}
	public String getReward_random() {
		return reward_random;
	}

	public void setReward_random(String reward_random) {
		this.reward_random = reward_random;
	}
	public String getReward_final() {
		return reward_final;
	}

	public void setReward_final(String reward_final) {
		this.reward_final = reward_final;
	}
	public String getReward_show() {
		return reward_show;
	}

	public void setReward_show(String reward_show) {
		this.reward_show = reward_show;
	}
}
