package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_11_sales")
public class Active11SalesTemplate {
	private Integer id;
	private Integer start_time;
	private Integer end_time;
	private String reward;
	private Integer price_base;
	private Integer recharge_gift_id;
	private Integer buy_times;
	private Integer buy_times_single;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStart_time() {
		return start_time;
	}

	public void setStart_time(Integer start_time) {
		this.start_time = start_time;
	}
	public Integer getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Integer end_time) {
		this.end_time = end_time;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getPrice_base() {
		return price_base;
	}

	public void setPrice_base(Integer price_base) {
		this.price_base = price_base;
	}
	public Integer getRecharge_gift_id() {
		return recharge_gift_id;
	}

	public void setRecharge_gift_id(Integer recharge_gift_id) {
		this.recharge_gift_id = recharge_gift_id;
	}
	public Integer getBuy_times() {
		return buy_times;
	}

	public void setBuy_times(Integer buy_times) {
		this.buy_times = buy_times;
	}
	public Integer getBuy_times_single() {
		return buy_times_single;
	}

	public void setBuy_times_single(Integer buy_times_single) {
		this.buy_times_single = buy_times_single;
	}
}
