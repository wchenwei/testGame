package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("kf_season_pass_level")
public class KfSeasonPassLevelTemplate {
	private Integer id;
	private Integer pass_exp;
	private Integer pass_exp_total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPass_exp() {
		return pass_exp;
	}

	public void setPass_exp(Integer pass_exp) {
		this.pass_exp = pass_exp;
	}
	public Integer getPass_exp_total() {
		return pass_exp_total;
	}

	public void setPass_exp_total(Integer pass_exp_total) {
		this.pass_exp_total = pass_exp_total;
	}
}
