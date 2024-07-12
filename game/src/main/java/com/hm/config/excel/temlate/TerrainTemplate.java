package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("terrain")
public class TerrainTemplate {
	private Integer id;
	private Integer quality;
	private Integer type;
	private Integer skill_id;
	private Integer substitute;
	private Integer terrain;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public Integer getSubstitute() {
		return substitute;
	}

	public void setSubstitute(Integer substitute) {
		this.substitute = substitute;
	}
	public Integer getTerrain() {
		return terrain;
	}

	public void setTerrain(Integer terrain) {
		this.terrain = terrain;
	}
}
