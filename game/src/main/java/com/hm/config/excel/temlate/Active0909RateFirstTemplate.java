package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0909_rate_first")
public class Active0909RateFirstTemplate {
	private Integer recharge_down;
	private Integer recharge_up;
	private Float rate_down;
	private Float rate_up;
	private String lucky;

	public Integer getRecharge_down() {
		return recharge_down;
	}

	public void setRecharge_down(Integer recharge_down) {
		this.recharge_down = recharge_down;
	}
	public Integer getRecharge_up() {
		return recharge_up;
	}

	public void setRecharge_up(Integer recharge_up) {
		this.recharge_up = recharge_up;
	}
	public Float getRate_down() {
		return rate_down;
	}

	public void setRate_down(Float rate_down) {
		this.rate_down = rate_down;
	}
	public Float getRate_up() {
		return rate_up;
	}

	public void setRate_up(Float rate_up) {
		this.rate_up = rate_up;
	}
	public String getLucky() {
		return lucky;
	}

	public void setLucky(String lucky) {
		this.lucky = lucky;
	}
}
