package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("military_lineup_assistance")
public class MilitaryLineupAssistanceTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private Long power;
	private String attri;

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
	public Long getPower() {
		return power;
	}

	public void setPower(Long power) {
		this.power = power;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
}
