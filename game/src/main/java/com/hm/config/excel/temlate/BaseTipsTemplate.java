package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("base_tips")
public class BaseTipsTemplate {
	private Integer id;
	private String random_tips;
	private String fail_tips;
	private String fail_name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRandom_tips() {
		return random_tips;
	}

	public void setRandom_tips(String random_tips) {
		this.random_tips = random_tips;
	}
	public String getFail_tips() {
		return fail_tips;
	}

	public void setFail_tips(String fail_tips) {
		this.fail_tips = fail_tips;
	}
	public String getFail_name() {
		return fail_name;
	}

	public void setFail_name(String fail_name) {
		this.fail_name = fail_name;
	}
}
