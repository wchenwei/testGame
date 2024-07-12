package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("museum_level")
public class MuseumLevelTemplate {
	private Integer id;
	private Integer mark_value;
	private Float attri;
	private String circle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMark_value() {
		return mark_value;
	}

	public void setMark_value(Integer mark_value) {
		this.mark_value = mark_value;
	}
	public Float getAttri() {
		return attri;
	}

	public void setAttri(Float attri) {
		this.attri = attri;
	}
	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}
}
