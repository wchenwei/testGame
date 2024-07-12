package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("battle_buff")
public class BattleBuffTemplate {
	private Integer id;
	private Integer type;
	private Integer typeid;
	private String bufficon;

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
	public Integer getTypeid() {
		return typeid;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}
	public String getBufficon() {
		return bufficon;
	}

	public void setBufficon(String bufficon) {
		this.bufficon = bufficon;
	}
}
