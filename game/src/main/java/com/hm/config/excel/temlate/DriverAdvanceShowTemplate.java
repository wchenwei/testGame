package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("driver_advance_show")
public class DriverAdvanceShowTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String attri_choose;

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
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getAttri_choose() {
		return attri_choose;
	}

	public void setAttri_choose(String attri_choose) {
		this.attri_choose = attri_choose;
	}
}
