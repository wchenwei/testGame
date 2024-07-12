package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_rescorce_fight")
public class KfRescorceFightTemplate {
	private Integer id;
	private Integer type;
	private Integer mine_area;
	private Integer is_poor;
	private Integer level;
	private Integer id_sub;
	private Integer quality;
	private Integer drop;
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
	public Integer getMine_area() {
		return mine_area;
	}

	public void setMine_area(Integer mine_area) {
		this.mine_area = mine_area;
	}
	public Integer getIs_poor() {
		return is_poor;
	}

	public void setIs_poor(Integer is_poor) {
		this.is_poor = is_poor;
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
	public Integer getDrop() {
		return drop;
	}

	public void setDrop(Integer drop) {
		this.drop = drop;
	}
	public Integer getScorce() {
		return scorce;
	}

	public void setScorce(Integer scorce) {
		this.scorce = scorce;
	}
}
