package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("forbid_word")
public class ForbidWordTemplate {
	private Integer id;
	private String forbid_word;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getForbid_word() {
		return forbid_word;
	}

	public void setForbid_word(String forbid_word) {
		this.forbid_word = forbid_word;
	}
}
