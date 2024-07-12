package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_flag")
public class GuildFlagTemplate {
	private Integer id;
	private String flag_icon;
	private String small_icon;
	private String cost;
	private Integer display;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getFlag_icon() {
		return flag_icon;
	}

	public void setFlag_icon(String flag_icon) {
		this.flag_icon = flag_icon;
	}
	public String getSmall_icon() {
		return small_icon;
	}

	public void setSmall_icon(String small_icon) {
		this.small_icon = small_icon;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}
}
