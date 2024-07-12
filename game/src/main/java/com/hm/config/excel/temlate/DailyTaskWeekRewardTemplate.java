package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_task_week_reward")
public class DailyTaskWeekRewardTemplate {
	private Integer id;
	private Integer week_actvie_lv;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer ad_id;
	private String reward_base;
	private String reward_high;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWeek_actvie_lv() {
		return week_actvie_lv;
	}

	public void setWeek_actvie_lv(Integer week_actvie_lv) {
		this.week_actvie_lv = week_actvie_lv;
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

	public Integer getAd_id() {
		return ad_id;
	}

	public void setAd_id(Integer ad_id) {
		this.ad_id = ad_id;
	}

	public String getReward_base() {
		return reward_base;
	}

	public void setReward_base(String reward_base) {
		this.reward_base = reward_base;
	}
	public String getReward_high() {
		return reward_high;
	}

	public void setReward_high(String reward_high) {
		this.reward_high = reward_high;
	}
}
