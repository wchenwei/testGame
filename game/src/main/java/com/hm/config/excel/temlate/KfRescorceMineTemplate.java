package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_rescorce_mine")
public class KfRescorceMineTemplate {
	private Integer id;
	private Integer player_lv;
	private Integer enemy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPlayer_lv() {
		return player_lv;
	}

	public void setPlayer_lv(Integer player_lv) {
		this.player_lv = player_lv;
	}
	public Integer getEnemy() {
		return enemy;
	}

	public void setEnemy(Integer enemy) {
		this.enemy = enemy;
	}
}
