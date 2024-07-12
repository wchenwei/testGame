package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_guild_weapon")
public class ItemGuildWeaponTemplate {
	private Integer id;
	private String paper_name;
	private String paper_icon;
	private String paper_desc;
	private Integer type;
	private Integer quality;
	private Integer rank;
	private Integer skill_id;
	private String recycle;
	private Float recycle_rate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPaper_name() {
		return paper_name;
	}

	public void setPaper_name(String paper_name) {
		this.paper_name = paper_name;
	}
	public String getPaper_icon() {
		return paper_icon;
	}

	public void setPaper_icon(String paper_icon) {
		this.paper_icon = paper_icon;
	}
	public String getPaper_desc() {
		return paper_desc;
	}

	public void setPaper_desc(String paper_desc) {
		this.paper_desc = paper_desc;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}
	public Float getRecycle_rate() {
		return recycle_rate;
	}

	public void setRecycle_rate(Float recycle_rate) {
		this.recycle_rate = recycle_rate;
	}
}
