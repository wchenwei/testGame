package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("random_task_intimacy")
public class RandomTaskIntimacyTemplate {
	private Integer id;
	private Integer intimacy;
	private String attribute_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIntimacy() {
		return intimacy;
	}

	public void setIntimacy(Integer intimacy) {
		this.intimacy = intimacy;
	}
	public String getAttribute_add() {
		return attribute_add;
	}

	public void setAttribute_add(String attribute_add) {
		this.attribute_add = attribute_add;
	}
}
