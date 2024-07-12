package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_77_movie")
public class Active77MovieTemplate {
	private Integer id;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer movie;
	private Integer type;
	private String reward;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getMovie() {
		return movie;
	}

	public void setMovie(Integer movie) {
		this.movie = movie;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
