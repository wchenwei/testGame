package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("parts")
public class PartsTemplate {
	private Integer id;
	private String piece_name;
	private String parts_name;
	private Integer quality;
	private String piece_icon;
	private String parts_icon;
	private String parts_desc;
	private String come_from;
	private Integer rank;
	private Integer sale;
	private String price;
	private Integer parts_exp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getPiece_name() {
		return piece_name;
	}

	public void setPiece_name(String piece_name) {
		this.piece_name = piece_name;
	}
	public String getParts_name() {
		return parts_name;
	}

	public void setParts_name(String parts_name) {
		this.parts_name = parts_name;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public String getPiece_icon() {
		return piece_icon;
	}

	public void setPiece_icon(String piece_icon) {
		this.piece_icon = piece_icon;
	}
	public String getParts_icon() {
		return parts_icon;
	}

	public void setParts_icon(String parts_icon) {
		this.parts_icon = parts_icon;
	}
	public String getParts_desc() {
		return parts_desc;
	}

	public void setParts_desc(String parts_desc) {
		this.parts_desc = parts_desc;
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
	public Integer getSale() {
		return sale;
	}

	public void setSale(Integer sale) {
		this.sale = sale;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getParts_exp() {
		return parts_exp;
	}

	public void setParts_exp(Integer parts_exp) {
		this.parts_exp = parts_exp;
	}
}
