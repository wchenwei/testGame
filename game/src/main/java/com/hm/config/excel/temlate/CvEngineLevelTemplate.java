package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("cv_engine_level")
public class CvEngineLevelTemplate {
	private Integer id;
	private Integer attri_type;
	private String attri;
	private String cost;
	private Integer unlock_level;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAttri_type() {
		return attri_type;
	}

	public void setAttri_type(Integer attri_type) {
		this.attri_type = attri_type;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getUnlock_level() {
		return unlock_level;
	}

	public void setUnlock_level(Integer unlock_level) {
		this.unlock_level = unlock_level;
	}
}
