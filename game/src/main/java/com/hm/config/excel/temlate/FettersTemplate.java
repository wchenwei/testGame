package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("fetters")
public class FettersTemplate {
	private Integer id;
	private String name;
	private String friend_id;
	private String star_num;
	private String attr_value;
	private String cost;
	private Integer belong;
	private Integer old_skill;
	private Integer new_skill;
	private Integer quality;

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
	public String getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(String friend_id) {
		this.friend_id = friend_id;
	}
	public String getStar_num() {
		return star_num;
	}

	public void setStar_num(String star_num) {
		this.star_num = star_num;
	}
	public String getAttr_value() {
		return attr_value;
	}

	public void setAttr_value(String attr_value) {
		this.attr_value = attr_value;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getBelong() {
		return belong;
	}

	public void setBelong(Integer belong) {
		this.belong = belong;
	}
	public Integer getOld_skill() {
		return old_skill;
	}

	public void setOld_skill(Integer old_skill) {
		this.old_skill = old_skill;
	}
	public Integer getNew_skill() {
		return new_skill;
	}

	public void setNew_skill(Integer new_skill) {
		this.new_skill = new_skill;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
}
