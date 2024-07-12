package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_medal_special_star")
public class PlayerMedalSpecialStarTemplate {
	private Integer id;
	private Integer medal_id;
	private Integer medal_lv;
	private Integer tank_type;
	private String attri;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMedal_id() {
		return medal_id;
	}

	public void setMedal_id(Integer medal_id) {
		this.medal_id = medal_id;
	}
	public Integer getMedal_lv() {
		return medal_lv;
	}

	public void setMedal_lv(Integer medal_lv) {
		this.medal_lv = medal_lv;
	}
	public Integer getTank_type() {
		return tank_type;
	}

	public void setTank_type(Integer tank_type) {
		this.tank_type = tank_type;
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
