package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("head")
public class HeadTemplate {
	private Integer id;
	private String name;
	private Integer type;
	private Integer stype;
	private Integer player_level;
	private Integer vip_level;
	private String icon;

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
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}
	public Integer getPlayer_level() {
		return player_level;
	}

	public void setPlayer_level(Integer player_level) {
		this.player_level = player_level;
	}
	public Integer getVip_level() {
		return vip_level;
	}

	public void setVip_level(Integer vip_level) {
		this.vip_level = vip_level;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
