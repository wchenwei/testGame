package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_enhance_break")
public class BuildingEnhanceBreakTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private String break_cost;
	private Integer	upgrade_num;

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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getBreak_cost() {
		return break_cost;
	}

	public void setBreak_cost(String break_cost) {
		this.break_cost = break_cost;
	}

	public Integer getUpgrade_num() {
		return upgrade_num;
	}

	public void setUpgrade_num(Integer upgrade_num) {
		this.upgrade_num = upgrade_num;
	}
	
	
}
