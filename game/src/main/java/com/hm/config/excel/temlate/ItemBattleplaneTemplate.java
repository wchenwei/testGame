package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_battleplane")
public class ItemBattleplaneTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String desc;
	private Integer quality;
	private Integer rank;
	private Integer skill_id;
	private String attri;
	private String cost_1;
	private Integer cost_extra;
	private Integer skill_cost;
	private String recycle;
	private Integer type;

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
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public String getCost_1() {
		return cost_1;
	}

	public void setCost_1(String cost_1) {
		this.cost_1 = cost_1;
	}
	public Integer getCost_extra() {
		return cost_extra;
	}

	public void setCost_extra(Integer cost_extra) {
		this.cost_extra = cost_extra;
	}
	public Integer getSkill_cost() {
		return skill_cost;
	}

	public void setSkill_cost(Integer skill_cost) {
		this.skill_cost = skill_cost;
	}
	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
