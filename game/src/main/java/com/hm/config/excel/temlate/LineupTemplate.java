package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("lineup")
public class LineupTemplate {
	private Integer id;
	private String line_name;
	private String lineup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}
	public String getLineup() {
		return lineup;
	}

	public void setLineup(String lineup) {
		this.lineup = lineup;
	}
}
