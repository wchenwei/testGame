package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_manor_fight_buff")
public class KfManorFightBuffTemplate {
	private Integer id;
	private Float type;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Float getType() {
		return type;
	}

	public void setType(Float type) {
		this.type = type;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
