package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("total_war_match_new")
public class TotalWarMatchNewTemplate {
	private Integer id;
	private String range;
	private Integer score;
	private Integer type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
