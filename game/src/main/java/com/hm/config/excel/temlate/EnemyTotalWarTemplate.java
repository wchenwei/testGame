package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("enemy_total_war")
public class EnemyTotalWarTemplate {
	private Integer id;
	private Integer level;
	private String name;
	private Integer head_icon;
	private Integer head_frame;
	private String enemy_config;
	private String power;
	private Integer group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}
}
