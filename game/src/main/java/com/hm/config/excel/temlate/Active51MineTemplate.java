package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_51_mine")
public class Active51MineTemplate {
	private Integer id;
	private Integer level;
	private Integer column;
	private Integer brick_id;
	private Integer is_treasure;

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
	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}
	public Integer getBrick_id() {
		return brick_id;
	}

	public void setBrick_id(Integer brick_id) {
		this.brick_id = brick_id;
	}
	public Integer getIs_treasure() {
		return is_treasure;
	}

	public void setIs_treasure(Integer is_treasure) {
		this.is_treasure = is_treasure;
	}
}
