package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0909_boss_damage")
public class Active0909BossDamageTemplate {
	private Integer id;
	private Integer down;
	private Integer up;
	private String crit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDown() {
		return down;
	}

	public void setDown(Integer down) {
		this.down = down;
	}
	public Integer getUp() {
		return up;
	}

	public void setUp(Integer up) {
		this.up = up;
	}
	public String getCrit() {
		return crit;
	}

	public void setCrit(String crit) {
		this.crit = crit;
	}
}
