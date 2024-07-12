package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("cv_island")
public class CvIslandTemplate {
	private Integer id;
	private String name;
	private String icon;
	private Integer type;
	private Integer level;
	private Integer unlock_engine_cabin_lv;
	private Integer unlock_engine_room_lv;
	private String attri;
	private String cost;
	private Integer skill_id;
	private String attri_extra;
	private Integer unlock_show;
	private Integer skillAddType;

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
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getUnlock_engine_cabin_lv() {
		return unlock_engine_cabin_lv;
	}

	public void setUnlock_engine_cabin_lv(Integer unlock_engine_cabin_lv) {
		this.unlock_engine_cabin_lv = unlock_engine_cabin_lv;
	}
	public Integer getUnlock_engine_room_lv() {
		return unlock_engine_room_lv;
	}

	public void setUnlock_engine_room_lv(Integer unlock_engine_room_lv) {
		this.unlock_engine_room_lv = unlock_engine_room_lv;
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
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public String getAttri_extra() {
		return attri_extra;
	}

	public void setAttri_extra(String attri_extra) {
		this.attri_extra = attri_extra;
	}
	public Integer getUnlock_show() {
		return unlock_show;
	}

	public void setUnlock_show(Integer unlock_show) {
		this.unlock_show = unlock_show;
	}
	public Integer getSkillAddType() {
		return skillAddType;
	}

	public void setSkillAddType(Integer skillAddType) {
		this.skillAddType = skillAddType;
	}
}
