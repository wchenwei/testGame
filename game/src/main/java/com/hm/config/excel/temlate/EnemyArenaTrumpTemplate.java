package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("enemy_arena_trump")
public class EnemyArenaTrumpTemplate {
	private Integer id;
	private Integer floor_id;
	private Integer rank;
	private String name;
	private Integer head_icon;
	private Integer level;
	private Integer level_down;
	private Integer group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(Integer floor_id) {
		this.floor_id = floor_id;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getLevel_down() {
		return level_down;
	}

	public void setLevel_down(Integer level_down) {
		this.level_down = level_down;
	}
	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}
}
