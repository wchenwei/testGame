package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_sweeper_event")
public class RareTreasureEventTemplate {
	private Integer id;
	private String name;
	private String icon;
	private String para;

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
	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
}
