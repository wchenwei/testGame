package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("battleplane_star")
public class BattleplaneStarTemplate {
	private Integer id;
	private Integer battleplane_id;
	private Integer star;
	private String attri;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBattleplane_id() {
		return battleplane_id;
	}

	public void setBattleplane_id(Integer battleplane_id) {
		this.battleplane_id = battleplane_id;
	}
	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
}
