package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_enhance_attri")
public class BuildingEnhanceAttriTemplate {
	private Integer id;
	private Integer type;
	private Integer rank;
	private Integer level;
	private Integer need_id;
	private Integer need_num;

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
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getNeed_id() {
		return need_id;
	}

	public void setNeed_id(Integer need_id) {
		this.need_id = need_id;
	}
	public Integer getNeed_num() {
		return need_num;
	}

	public void setNeed_num(Integer need_num) {
		this.need_num = need_num;
	}
}
