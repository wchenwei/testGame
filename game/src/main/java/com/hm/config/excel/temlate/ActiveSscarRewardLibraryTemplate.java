package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_SScar_reward_library")
public class ActiveSscarRewardLibraryTemplate {
	private Integer id;
	private Integer reward_type;
	private Integer pos;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private String reward;
	private Integer buy_limit;
	private Integer score_price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getReward_type() {
		return reward_type;
	}

	public void setReward_type(Integer reward_type) {
		this.reward_type = reward_type;
	}
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
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
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getBuy_limit() {
		return buy_limit;
	}

	public void setBuy_limit(Integer buy_limit) {
		this.buy_limit = buy_limit;
	}
	public Integer getScore_price() {
		return score_price;
	}

	public void setScore_price(Integer score_price) {
		this.score_price = score_price;
	}
}
