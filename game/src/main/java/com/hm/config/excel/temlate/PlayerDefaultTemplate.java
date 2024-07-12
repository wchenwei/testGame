package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_default")
public class PlayerDefaultTemplate {
	private Integer index;
	private Integer type;
	private Integer id;
	private Integer num;
	private String hit;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}
}
