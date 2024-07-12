package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("res_manage")
public class ResManageTemplate {
	private Integer id;
	private String icon;
	private String name;
	private String call_item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getCall_item() {
		return call_item;
	}

	public void setCall_item(String call_item) {
		this.call_item = call_item;
	}
}
