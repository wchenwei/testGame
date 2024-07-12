package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_discount_random")
public class ActiveDiscountRandomTemplate {
	private Integer id;
	private Integer times_down;
	private Integer times_up;
	private Float discount_down;
	private Float discount_up;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTimes_down() {
		return times_down;
	}

	public void setTimes_down(Integer times_down) {
		this.times_down = times_down;
	}
	public Integer getTimes_up() {
		return times_up;
	}

	public void setTimes_up(Integer times_up) {
		this.times_up = times_up;
	}
	public Float getDiscount_down() {
		return discount_down;
	}

	public void setDiscount_down(Float discount_down) {
		this.discount_down = discount_down;
	}
	public Float getDiscount_up() {
		return discount_up;
	}

	public void setDiscount_up(Float discount_up) {
		this.discount_up = discount_up;
	}
}
