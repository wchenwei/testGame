package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_open_service")
public class ActiveOpenServiceTemplate {
	private Integer index;
	private Integer id;
	private Integer open_day;
	private Integer last_day;
	private String para;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOpen_day() {
		return open_day;
	}

	public void setOpen_day(Integer open_day) {
		this.open_day = open_day;
	}
	public Integer getLast_day() {
		return last_day;
	}

	public void setLast_day(Integer last_day) {
		this.last_day = last_day;
	}
	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
}
