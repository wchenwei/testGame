package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("oil_cost")
public class OilCostTemplate {
	private Integer id;
	private Long power_down;
	private Long power_up;
	private Float power_proportion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Long getPower_down() {
		return power_down;
	}

	public void setPower_down(Long power_down) {
		this.power_down = power_down;
	}
	public Long getPower_up() {
		return power_up;
	}

	public void setPower_up(Long power_up) {
		this.power_up = power_up;
	}
	public Float getPower_proportion() {
		return power_proportion;
	}

	public void setPower_proportion(Float power_proportion) {
		this.power_proportion = power_proportion;
	}
}
