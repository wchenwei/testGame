package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("fetter_circle")
public class FetterCircleTemplate {
	private Integer id;
	private Integer quality;
	private Integer level;
	private String attri;

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
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
}
