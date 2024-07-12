package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_crew_piece")
public class TankCrewPieceTemplate {
	private Integer id;
	private Integer position;
	private String name;
	private String desc;
	private String icon;
	private Integer crew;
	private Integer quality;
	private String come_from;
	private Integer rank;

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
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getCrew() {
		return crew;
	}

	public void setCrew(Integer crew) {
		this.crew = crew;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getCome_from() {
		return come_from;
	}

	public void setCome_from(String come_from) {
		this.come_from = come_from;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
