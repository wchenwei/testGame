package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_tool")
public class ItemToolTemplate {
	private Integer id;
	private String paper_name;
	private String paper_icon;
	private String paper_desc;
	private Integer quality;
	private Integer rank;
	private String attri;
	private String from_group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPaper_name() {
		return paper_name;
	}

	public void setPaper_name(String paper_name) {
		this.paper_name = paper_name;
	}
	public String getPaper_icon() {
		return paper_icon;
	}

	public void setPaper_icon(String paper_icon) {
		this.paper_icon = paper_icon;
	}
	public String getPaper_desc() {
		return paper_desc;
	}

	public void setPaper_desc(String paper_desc) {
		this.paper_desc = paper_desc;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public String getFrom_group() {
		return from_group;
	}

	public void setFrom_group(String from_group) {
		this.from_group = from_group;
	}
}
