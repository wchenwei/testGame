package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_dream_elite")
public class MissionDreamEliteTemplate {
	private Integer id;
	private Integer chapter_id;
	private Integer level;
	private Integer before_mission;
	private Integer next_mission;
	private String icon;
	private String name;
	private Integer head_icon;
	private Integer head_frame;
	private Integer mission_map;
	private Integer start_point;
	private String enemy_config;
	private String power;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getChapter_id() {
		return chapter_id;
	}

	public void setChapter_id(Integer chapter_id) {
		this.chapter_id = chapter_id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getBefore_mission() {
		return before_mission;
	}

	public void setBefore_mission(Integer before_mission) {
		this.before_mission = before_mission;
	}
	public Integer getNext_mission() {
		return next_mission;
	}

	public void setNext_mission(Integer next_mission) {
		this.next_mission = next_mission;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getHead_icon() {
		return head_icon;
	}

	public void setHead_icon(Integer head_icon) {
		this.head_icon = head_icon;
	}
	public Integer getHead_frame() {
		return head_frame;
	}

	public void setHead_frame(Integer head_frame) {
		this.head_frame = head_frame;
	}
	public Integer getMission_map() {
		return mission_map;
	}

	public void setMission_map(Integer mission_map) {
		this.mission_map = mission_map;
	}
	public Integer getStart_point() {
		return start_point;
	}

	public void setStart_point(Integer start_point) {
		this.start_point = start_point;
	}
	public String getEnemy_config() {
		return enemy_config;
	}

	public void setEnemy_config(String enemy_config) {
		this.enemy_config = enemy_config;
	}
	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}
}
