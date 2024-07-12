package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("swat_config")
public class SwatConfigTemplate {
	private Integer id;
	private Integer swat_level;
	private String open_level;
	private String swat_name;
	private String swat_desc;
	private String swat_bg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSwat_level() {
		return swat_level;
	}

	public void setSwat_level(Integer swat_level) {
		this.swat_level = swat_level;
	}
	public String getOpen_level() {
		return open_level;
	}

	public void setOpen_level(String open_level) {
		this.open_level = open_level;
	}
	public String getSwat_name() {
		return swat_name;
	}

	public void setSwat_name(String swat_name) {
		this.swat_name = swat_name;
	}
	public String getSwat_desc() {
		return swat_desc;
	}

	public void setSwat_desc(String swat_desc) {
		this.swat_desc = swat_desc;
	}
	public String getSwat_bg() {
		return swat_bg;
	}

	public void setSwat_bg(String swat_bg) {
		this.swat_bg = swat_bg;
	}
}
