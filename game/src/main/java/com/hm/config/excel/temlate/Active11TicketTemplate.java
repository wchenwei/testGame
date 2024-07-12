package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_11_ticket")
public class Active11TicketTemplate {
	private Integer id;
	private Integer sever_lv_down;
	private Integer sever_lv_up;
	private Integer num;
	private Integer weight;
	private Integer limit;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSever_lv_down() {
		return sever_lv_down;
	}

	public void setSever_lv_down(Integer sever_lv_down) {
		this.sever_lv_down = sever_lv_down;
	}
	public Integer getSever_lv_up() {
		return sever_lv_up;
	}

	public void setSever_lv_up(Integer sever_lv_up) {
		this.sever_lv_up = sever_lv_up;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
