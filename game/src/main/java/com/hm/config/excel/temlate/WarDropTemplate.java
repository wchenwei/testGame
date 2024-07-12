package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("war_drop")
public class WarDropTemplate {
	private Integer id;
	private String drop_item;
	private String level;
	private Float rate;
	private Integer limit_num;
	private Integer drop_case;
	private String case_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getDrop_item() {
		return drop_item;
	}

	public void setDrop_item(String drop_item) {
		this.drop_item = drop_item;
	}
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
	public Integer getLimit_num() {
		return limit_num;
	}

	public void setLimit_num(Integer limit_num) {
		this.limit_num = limit_num;
	}
	public Integer getDrop_case() {
		return drop_case;
	}

	public void setDrop_case(Integer drop_case) {
		this.drop_case = drop_case;
	}
	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
}
