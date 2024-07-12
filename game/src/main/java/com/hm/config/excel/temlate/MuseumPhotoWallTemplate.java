package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("museum_photo_wall")
public class MuseumPhotoWallTemplate {
	private Integer id;
	private Integer chapter;
	private Integer wall_id;
	private Integer tank_id;
	private Integer tank_photo_id;
	private Integer cost;
	private String arrti;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	public Integer getWall_id() {
		return wall_id;
	}

	public void setWall_id(Integer wall_id) {
		this.wall_id = wall_id;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public Integer getTank_photo_id() {
		return tank_photo_id;
	}

	public void setTank_photo_id(Integer tank_photo_id) {
		this.tank_photo_id = tank_photo_id;
	}
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public String getArrti() {
		return arrti;
	}

	public void setArrti(String arrti) {
		this.arrti = arrti;
	}
}
