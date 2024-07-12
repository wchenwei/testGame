package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew")
public class TankCrewTemplate {
	private Integer id;
	private Integer position;
	private String name;
	private String icon;
	private String crew_piece;
	private Integer quality;
	private Integer suit_type;
	private Integer attri_type;
	private String recycle;
	private String access;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
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
	public String getCrew_piece() {
		return crew_piece;
	}

	public void setCrew_piece(String crew_piece) {
		this.crew_piece = crew_piece;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getSuit_type() {
		return suit_type;
	}

	public void setSuit_type(Integer suit_type) {
		this.suit_type = suit_type;
	}
	public Integer getAttri_type() {
		return attri_type;
	}

	public void setAttri_type(Integer attri_type) {
		this.attri_type = attri_type;
	}
	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}
