package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("pop_config")
public class PopConfigTemplate {
	private Integer id;
	private String name;
	private String resource;
	private Integer unclock;

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
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	public Integer getUnclock() {
		return unclock;
	}

	public void setUnclock(Integer unclock) {
		this.unclock = unclock;
	}
}
