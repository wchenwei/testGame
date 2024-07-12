package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_watch_exp")
public class WarWatchExpTemplate {
	private Integer id;
	private Integer type;
	private Integer exp;
	private Integer num;
	private Integer need_vip;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getNeed_vip() {
		return need_vip;
	}

	public void setNeed_vip(Integer need_vip) {
		this.need_vip = need_vip;
	}
}
