package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_photo")
public class ItemPhotoTemplate {
	private Integer paper_id;
	private Integer tank_id;
	private String paper_name;
	private String paper_icon;
	private String paper_desc;
	private Integer quality;
	private Integer rank;
	private String recycle;

	public Integer getPaper_id() {
		return paper_id;
	}

	public void setPaper_id(Integer paper_id) {
		this.paper_id = paper_id;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
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
	public String getRecycle() {
		return recycle;
	}

	public void setRecycle(String recycle) {
		this.recycle = recycle;
	}
}
