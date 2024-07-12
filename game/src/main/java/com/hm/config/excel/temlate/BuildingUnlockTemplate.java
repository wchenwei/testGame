package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("building_unlock")
public class BuildingUnlockTemplate {
	private Integer id;
	private String name;
	private Integer build_type;
	private String icon;
	private Float icon_scale;
	private Float x;
	private Float y;
	private Integer level;
	private Integer location;
	private Integer open_base;
	private Integer lv_player;
	private Integer lv_mbuild;
	private String build_des;

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
	public Integer getBuild_type() {
		return build_type;
	}

	public void setBuild_type(Integer build_type) {
		this.build_type = build_type;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Float getIcon_scale() {
		return icon_scale;
	}

	public void setIcon_scale(Float icon_scale) {
		this.icon_scale = icon_scale;
	}
	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}
	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public Integer getOpen_base() {
		return open_base;
	}

	public void setOpen_base(Integer open_base) {
		this.open_base = open_base;
	}
	public Integer getLv_player() {
		return lv_player;
	}

	public void setLv_player(Integer lv_player) {
		this.lv_player = lv_player;
	}
	public Integer getLv_mbuild() {
		return lv_mbuild;
	}

	public void setLv_mbuild(Integer lv_mbuild) {
		this.lv_mbuild = lv_mbuild;
	}
	public String getBuild_des() {
		return build_des;
	}

	public void setBuild_des(String build_des) {
		this.build_des = build_des;
	}
}
