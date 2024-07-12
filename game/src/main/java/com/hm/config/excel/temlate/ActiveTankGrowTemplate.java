package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_tank_grow")
public class ActiveTankGrowTemplate {
	private Integer id;
	private Integer open_lv;
	private String tank_id;
	private String finish;
	private String reward;
	private String tankRareType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOpen_lv() {
		return open_lv;
	}

	public void setOpen_lv(Integer open_lv) {
		this.open_lv = open_lv;
	}
	public String getTank_id() {
		return tank_id;
	}

	public void setTank_id(String tank_id) {
		this.tank_id = tank_id;
	}
	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getTankRareType() {
		return tankRareType;
	}

	public void setTankRareType(String tankRareType) {
		this.tankRareType = tankRareType;
	}
}
