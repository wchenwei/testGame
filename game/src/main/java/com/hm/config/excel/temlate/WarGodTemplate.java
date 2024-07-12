package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_god")
public class WarGodTemplate {
	private Integer level;
	private Long power_total;
	private Integer atk;
	private Integer def;
	private Integer hp;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getPower_total() {
		return power_total;
	}

	public void setPower_total(Long power_total) {
		this.power_total = power_total;
	}
	public Integer getAtk() {
		return atk;
	}

	public void setAtk(Integer atk) {
		this.atk = atk;
	}
	public Integer getDef() {
		return def;
	}

	public void setDef(Integer def) {
		this.def = def;
	}
	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}
}
