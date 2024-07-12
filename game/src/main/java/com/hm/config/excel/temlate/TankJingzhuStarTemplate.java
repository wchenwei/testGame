package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_jingzhu_star")
public class TankJingzhuStarTemplate {
	private Integer id;
	private Integer star_level;
	private Float buff;
	private Integer exp;
	private Integer exp_total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStar_level() {
		return star_level;
	}

	public void setStar_level(Integer star_level) {
		this.star_level = star_level;
	}
	public Float getBuff() {
		return buff;
	}

	public void setBuff(Float buff) {
		this.buff = buff;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getExp_total() {
		return exp_total;
	}

	public void setExp_total(Integer exp_total) {
		this.exp_total = exp_total;
	}
}
