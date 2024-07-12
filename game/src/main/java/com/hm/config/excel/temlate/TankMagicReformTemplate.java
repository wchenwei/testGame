package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_magic_reform")
public class TankMagicReformTemplate {
	private Integer id;
	private Integer tank_id;
	private Integer change_num_left;
	private Integer change_num_right;
	private Integer weight;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public Integer getChange_num_left() {
		return change_num_left;
	}

	public void setChange_num_left(Integer change_num_left) {
		this.change_num_left = change_num_left;
	}
	public Integer getChange_num_right() {
		return change_num_right;
	}

	public void setChange_num_right(Integer change_num_right) {
		this.change_num_right = change_num_right;
	}
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
