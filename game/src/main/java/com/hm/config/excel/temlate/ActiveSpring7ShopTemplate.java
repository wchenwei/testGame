package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_spring7_shop")
public class ActiveSpring7ShopTemplate {
	private Integer id;
	private Integer type;
	private Integer loacation;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private String reward;
	private String price;
	private Integer limit;
	private Integer recharge_id;

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
	public Integer getLoacation() {
		return loacation;
	}

	public void setLoacation(Integer loacation) {
		this.loacation = loacation;
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
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getRecharge_id() {
		return recharge_id;
	}

	public void setRecharge_id(Integer recharge_id) {
		this.recharge_id = recharge_id;
	}
}
