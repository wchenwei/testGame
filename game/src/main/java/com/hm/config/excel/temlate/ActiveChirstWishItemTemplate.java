package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_chirst_wish_item")
public class ActiveChirstWishItemTemplate {
	private Integer id;
	private String wish;
	private Integer wish_id;
	private Integer player_lv_down;
	private Integer player_lv_up;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getWish() {
		return wish;
	}

	public void setWish(String wish) {
		this.wish = wish;
	}
	public Integer getWish_id() {
		return wish_id;
	}

	public void setWish_id(Integer wish_id) {
		this.wish_id = wish_id;
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
}
