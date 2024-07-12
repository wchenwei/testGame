package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_recharge_4day_level")
public class ActiveRecharge4dayLevelTemplate {
	private Integer id;
	private Integer charge_gold;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCharge_gold() {
		return charge_gold;
	}

	public void setCharge_gold(Integer charge_gold) {
		this.charge_gold = charge_gold;
	}
}
