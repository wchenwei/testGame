package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("daily_task_week_recharge")
public class DailyTaskWeekRechargeTemplate {
	private Integer id;
	private Integer type;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer recharge_gift_id;
	private String desc;
	private Integer active_points_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	public Integer getRecharge_gift_id() {
		return recharge_gift_id;
	}

	public void setRecharge_gift_id(Integer recharge_gift_id) {
		this.recharge_gift_id = recharge_gift_id;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getActive_points_add() {
		return active_points_add;
	}

	public void setActive_points_add(Integer active_points_add) {
		this.active_points_add = active_points_add;
	}
}
