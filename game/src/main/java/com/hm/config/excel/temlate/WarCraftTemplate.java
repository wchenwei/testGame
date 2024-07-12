package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_craft")
public class WarCraftTemplate {
	private Integer id;
	private Integer book;
	private Integer chapter;
	private Integer level;
	private Integer attri_type;
	private String attri;
	private String cost;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBook() {
		return book;
	}

	public void setBook(Integer book) {
		this.book = book;
	}
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getAttri_type() {
		return attri_type;
	}

	public void setAttri_type(Integer attri_type) {
		this.attri_type = attri_type;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
