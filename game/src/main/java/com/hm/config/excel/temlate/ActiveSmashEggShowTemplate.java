package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_smash_egg_show")
public class ActiveSmashEggShowTemplate {
	private Integer id;
	private String title;
	private Integer quality;
	private String content;
	private String icon;
	private Integer is_show;
	private Integer is_item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getIs_show() {
		return is_show;
	}

	public void setIs_show(Integer is_show) {
		this.is_show = is_show;
	}
	public Integer getIs_item() {
		return is_item;
	}

	public void setIs_item(Integer is_item) {
		this.is_item = is_item;
	}
}
