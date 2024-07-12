package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_spring7_road")
public class ActiveSpring7RoadTemplate {
	private Integer id;
	private Integer day;
	private Integer type;
	private Integer sub_type;
	private Integer shop_type;
	private String task;
	private String name;
	private String desc;
	private String building;
	private Integer can_pass;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getSub_type() {
		return sub_type;
	}

	public void setSub_type(Integer sub_type) {
		this.sub_type = sub_type;
	}
	public Integer getShop_type() {
		return shop_type;
	}

	public void setShop_type(Integer shop_type) {
		this.shop_type = shop_type;
	}
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}
	public Integer getCan_pass() {
		return can_pass;
	}

	public void setCan_pass(Integer can_pass) {
		this.can_pass = can_pass;
	}
}
