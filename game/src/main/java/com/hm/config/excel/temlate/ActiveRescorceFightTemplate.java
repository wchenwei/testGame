package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_rescorce_fight")
public class ActiveRescorceFightTemplate {
	private Integer id;
	private Integer type;
	private Integer level;
	private Integer id_sub;
	private Integer quality;
	private String drop;
	private Integer scorce;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getId_sub() {
		return id_sub;
	}

	public void setId_sub(Integer id_sub) {
		this.id_sub = id_sub;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getDrop() {
		return drop;
	}

	public void setDrop(String drop) {
		this.drop = drop;
	}
	public Integer getScorce() {
		return scorce;
	}

	public void setScorce(Integer scorce) {
		this.scorce = scorce;
	}
}
