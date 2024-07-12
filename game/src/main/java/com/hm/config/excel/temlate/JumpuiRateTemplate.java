package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("jumpui_rate")
public class JumpuiRateTemplate {
	private Integer id;
	private String info;
	private Integer rate;
	private String path;
	private Integer condition;
	private String conditionParam;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}
	public String getConditionParam() {
		return conditionParam;
	}

	public void setConditionParam(String conditionParam) {
		this.conditionParam = conditionParam;
	}
}
