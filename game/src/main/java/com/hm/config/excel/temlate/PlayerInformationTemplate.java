package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("player_information")
public class PlayerInformationTemplate {
	private Integer id;
	private String player_head;
	private String player_show;
	private Integer base_unclock;
	private String bones_path;
	private String bones_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlayer_head() {
		return player_head;
	}

	public void setPlayer_head(String player_head) {
		this.player_head = player_head;
	}
	public String getPlayer_show() {
		return player_show;
	}

	public void setPlayer_show(String player_show) {
		this.player_show = player_show;
	}
	public Integer getBase_unclock() {
		return base_unclock;
	}

	public void setBase_unclock(Integer base_unclock) {
		this.base_unclock = base_unclock;
	}
	public String getBones_path() {
		return bones_path;
	}

	public void setBones_path(String bones_path) {
		this.bones_path = bones_path;
	}
	public String getBones_name() {
		return bones_name;
	}

	public void setBones_name(String bones_name) {
		this.bones_name = bones_name;
	}
}
