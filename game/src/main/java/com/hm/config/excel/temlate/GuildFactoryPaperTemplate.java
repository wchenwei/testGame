package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_factory_paper")
public class GuildFactoryPaperTemplate {
	private Integer id;
	private Integer quality;
	private String icon;
	private String library;
	private Integer build_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}
	public Integer getBuild_time() {
		return build_time;
	}

	public void setBuild_time(Integer build_time) {
		this.build_time = build_time;
	}
}
