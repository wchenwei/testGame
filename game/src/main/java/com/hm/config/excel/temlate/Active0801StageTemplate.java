package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0801_stage")
public class Active0801StageTemplate {
	private Integer id;
	private String icon_resource;
	private String title_resource;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
	public String getTitle_resource() {
		return title_resource;
	}

	public void setTitle_resource(String title_resource) {
		this.title_resource = title_resource;
	}
}
