package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew_upgrade")
public class TankCrewUpgradeTemplate {
	private Integer id;
	private Integer attri_type;
	private Integer attri_lv;
	private String attri;
	private String cost;

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
	public Integer getAttri_lv() {
		return attri_lv;
	}

	public void setAttri_lv(Integer attri_lv) {
		this.attri_lv = attri_lv;
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
}
