package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tec_type")
public class TecTypeTemplate {
	private Integer id;
	private Integer tec_type;
	private String tec_name;
	private String tec_sec;
	private String tec_icon;
	private Integer rows;
	private Integer column;
	private Integer arrow;
	private Integer lv_base;
	private Integer unlock_lv;
	private Integer unlock_tec;
	private Integer tec_quality;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTec_type() {
		return tec_type;
	}

	public void setTec_type(Integer tec_type) {
		this.tec_type = tec_type;
	}
	public String getTec_name() {
		return tec_name;
	}

	public void setTec_name(String tec_name) {
		this.tec_name = tec_name;
	}
	public String getTec_sec() {
		return tec_sec;
	}

	public void setTec_sec(String tec_sec) {
		this.tec_sec = tec_sec;
	}
	public String getTec_icon() {
		return tec_icon;
	}

	public void setTec_icon(String tec_icon) {
		this.tec_icon = tec_icon;
	}
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}
	public Integer getArrow() {
		return arrow;
	}

	public void setArrow(Integer arrow) {
		this.arrow = arrow;
	}
	public Integer getLv_base() {
		return lv_base;
	}

	public void setLv_base(Integer lv_base) {
		this.lv_base = lv_base;
	}
	public Integer getUnlock_lv() {
		return unlock_lv;
	}

	public void setUnlock_lv(Integer unlock_lv) {
		this.unlock_lv = unlock_lv;
	}
	public Integer getUnlock_tec() {
		return unlock_tec;
	}

	public void setUnlock_tec(Integer unlock_tec) {
		this.unlock_tec = unlock_tec;
	}
	public Integer getTec_quality() {
		return tec_quality;
	}

	public void setTec_quality(Integer tec_quality) {
		this.tec_quality = tec_quality;
	}
}
