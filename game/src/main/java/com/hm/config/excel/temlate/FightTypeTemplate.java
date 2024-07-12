package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("fight_type")
public class FightTypeTemplate {
	private Integer fight_type;
	private Integer time_initial;
	private String cost;

	public Integer getFight_type() {
		return fight_type;
	}

	public void setFight_type(Integer fight_type) {
		this.fight_type = fight_type;
	}
	public Integer getTime_initial() {
		return time_initial;
	}

	public void setTime_initial(Integer time_initial) {
		this.time_initial = time_initial;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
