package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("module_opentime")
public class ModuleOpentimeTemplate {
	private Integer id;
	private String name;
	private String open;

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
	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}
}
