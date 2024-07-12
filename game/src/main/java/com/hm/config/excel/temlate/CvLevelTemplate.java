package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("cv_level")
public class CvLevelTemplate {
	private Integer id;
	private String attri;
	private String cost;
	private Integer unlock_num;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getUnlock_num() {
		return unlock_num;
	}

	public void setUnlock_num(Integer unlock_num) {
		this.unlock_num = unlock_num;
	}
}
