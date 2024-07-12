package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tech_unlock")
public class TechUnlockTemplate {
	private Integer tech_type;
	private String tech_name;
	private String tech_desc;
	private Integer unlock_level;

	public Integer getTech_type() {
		return tech_type;
	}

	public void setTech_type(Integer tech_type) {
		this.tech_type = tech_type;
	}
	public String getTech_name() {
		return tech_name;
	}

	public void setTech_name(String tech_name) {
		this.tech_name = tech_name;
	}
	public String getTech_desc() {
		return tech_desc;
	}

	public void setTech_desc(String tech_desc) {
		this.tech_desc = tech_desc;
	}
	public Integer getUnlock_level() {
		return unlock_level;
	}

	public void setUnlock_level(Integer unlock_level) {
		this.unlock_level = unlock_level;
	}
}
