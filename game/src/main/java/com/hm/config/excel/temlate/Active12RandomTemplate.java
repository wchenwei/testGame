package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_12_random")
public class Active12RandomTemplate {
	private Integer id;
	private Integer days;
	private Integer buy_times;
	private Integer random_down;
	private Integer random_up;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
	public Integer getBuy_times() {
		return buy_times;
	}

	public void setBuy_times(Integer buy_times) {
		this.buy_times = buy_times;
	}
	public Integer getRandom_down() {
		return random_down;
	}

	public void setRandom_down(Integer random_down) {
		this.random_down = random_down;
	}
	public Integer getRandom_up() {
		return random_up;
	}

	public void setRandom_up(Integer random_up) {
		this.random_up = random_up;
	}
}
