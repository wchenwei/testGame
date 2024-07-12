package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("kf_season_pass_gift")
public class KfSeasonPassGiftTemplate {
	private Integer id;
	private Integer recharge_gift_id;
	private Integer pass_exp_add;
	private Integer limit_buy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRecharge_gift_id() {
		return recharge_gift_id;
	}

	public void setRecharge_gift_id(Integer recharge_gift_id) {
		this.recharge_gift_id = recharge_gift_id;
	}
	public Integer getPass_exp_add() {
		return pass_exp_add;
	}

	public void setPass_exp_add(Integer pass_exp_add) {
		this.pass_exp_add = pass_exp_add;
	}
	public Integer getLimit_buy() {
		return limit_buy;
	}

	public void setLimit_buy(Integer limit_buy) {
		this.limit_buy = limit_buy;
	}
}
