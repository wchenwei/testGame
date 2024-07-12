package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_tank_skill_piece")
public class ItemTankSkillPieceTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String dec;
	private Integer quality;
	private Integer skill_type;
	private Integer skill_id;
	private Integer piece_num_change;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getSkill_type() {
		return skill_type;
	}

	public void setSkill_type(Integer skill_type) {
		this.skill_type = skill_type;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Integer getPiece_num_change() {
		return piece_num_change;
	}

	public void setPiece_num_change(Integer piece_num_change) {
		this.piece_num_change = piece_num_change;
	}
}
