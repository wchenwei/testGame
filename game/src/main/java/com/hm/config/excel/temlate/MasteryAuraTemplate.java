package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mastery_aura")
public class MasteryAuraTemplate {
	private Integer id;
	private Integer tank_type;
	private Integer aura_type;
	private Integer level;
	private String attribute;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTank_type() {
		return tank_type;
	}

	public void setTank_type(Integer tank_type) {
		this.tank_type = tank_type;
	}
	public Integer getAura_type() {
		return aura_type;
	}

	public void setAura_type(Integer aura_type) {
		this.aura_type = aura_type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
