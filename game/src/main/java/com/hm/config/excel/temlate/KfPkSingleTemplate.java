package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_pk_single")
public class KfPkSingleTemplate {
	private Integer id;
	private String name;
	private Integer num;
	private Integer level_up;
	private Integer level_down;

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
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getLevel_up() {
		return level_up;
	}

	public void setLevel_up(Integer level_up) {
		this.level_up = level_up;
	}
	public Integer getLevel_down() {
		return level_down;
	}

	public void setLevel_down(Integer level_down) {
		this.level_down = level_down;
	}
}
