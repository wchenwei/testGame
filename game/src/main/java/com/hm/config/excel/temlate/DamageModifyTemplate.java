package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("damage_modify")
public class DamageModifyTemplate {
	private Integer id;
	private Integer atker_type;
	private Integer defender_type;
	private Float modify_value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAtker_type() {
		return atker_type;
	}

	public void setAtker_type(Integer atker_type) {
		this.atker_type = atker_type;
	}
	public Integer getDefender_type() {
		return defender_type;
	}

	public void setDefender_type(Integer defender_type) {
		this.defender_type = defender_type;
	}
	public Float getModify_value() {
		return modify_value;
	}

	public void setModify_value(Float modify_value) {
		this.modify_value = modify_value;
	}
}
