package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("policy_exp_limit")
public class PolicyExpLimitTemplate {
	private Integer level;
	private Integer exp_limit;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getExp_limit() {
		return exp_limit;
	}

	public void setExp_limit(Integer exp_limit) {
		this.exp_limit = exp_limit;
	}
}
