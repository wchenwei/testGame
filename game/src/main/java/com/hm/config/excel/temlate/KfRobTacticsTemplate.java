package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_rob_tactics")
public class KfRobTacticsTemplate {
	private Integer id;
	private String name;
	private String icon;
	private Integer unlock;
	private Integer use_time;
	private String value;

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
	public Integer getUnlock() {
		return unlock;
	}

	public void setUnlock(Integer unlock) {
		this.unlock = unlock;
	}
	public Integer getUse_time() {
		return use_time;
	}

	public void setUse_time(Integer use_time) {
		this.use_time = use_time;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
