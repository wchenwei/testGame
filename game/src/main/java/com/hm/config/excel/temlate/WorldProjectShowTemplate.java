package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_project_show")
public class WorldProjectShowTemplate {
	private Integer id;
	private String type;
	private String name;
	private String icon;
	private String desc;
	private Integer show_type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getShow_type() {
		return show_type;
	}

	public void setShow_type(Integer show_type) {
		this.show_type = show_type;
	}
}
