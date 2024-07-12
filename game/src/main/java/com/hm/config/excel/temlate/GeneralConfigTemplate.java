package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("general_config")
public class GeneralConfigTemplate {
	private Integer id;
	private String name;
	private String dec;
	private String resource;
	private String icon;
	private Integer quality;
	private String attribute;
	private Integer tank_id;
	private String attribute_extra;
	private Integer gegeral_piece_id;
	private String general_skill_unlock;
	private String general_skill_effect;

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
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public String getAttribute_extra() {
		return attribute_extra;
	}

	public void setAttribute_extra(String attribute_extra) {
		this.attribute_extra = attribute_extra;
	}
	public Integer getGegeral_piece_id() {
		return gegeral_piece_id;
	}

	public void setGegeral_piece_id(Integer gegeral_piece_id) {
		this.gegeral_piece_id = gegeral_piece_id;
	}
	public String getGeneral_skill_unlock() {
		return general_skill_unlock;
	}

	public void setGeneral_skill_unlock(String general_skill_unlock) {
		this.general_skill_unlock = general_skill_unlock;
	}
	public String getGeneral_skill_effect() {
		return general_skill_effect;
	}

	public void setGeneral_skill_effect(String general_skill_effect) {
		this.general_skill_effect = general_skill_effect;
	}
}
