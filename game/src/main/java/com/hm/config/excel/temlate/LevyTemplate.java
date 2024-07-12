package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("levy")
public class LevyTemplate {
	private Integer levy_count;
	private Integer cash_cost_gold;
	private String cash_crit_rate;
	private Integer oil_cost_gold;
	private String oil_crit_rate;

	public Integer getLevy_count() {
		return levy_count;
	}

	public void setLevy_count(Integer levy_count) {
		this.levy_count = levy_count;
	}
	public Integer getCash_cost_gold() {
		return cash_cost_gold;
	}

	public void setCash_cost_gold(Integer cash_cost_gold) {
		this.cash_cost_gold = cash_cost_gold;
	}
	public String getCash_crit_rate() {
		return cash_crit_rate;
	}

	public void setCash_crit_rate(String cash_crit_rate) {
		this.cash_crit_rate = cash_crit_rate;
	}
	public Integer getOil_cost_gold() {
		return oil_cost_gold;
	}

	public void setOil_cost_gold(Integer oil_cost_gold) {
		this.oil_cost_gold = oil_cost_gold;
	}
	public String getOil_crit_rate() {
		return oil_crit_rate;
	}

	public void setOil_crit_rate(String oil_crit_rate) {
		this.oil_crit_rate = oil_crit_rate;
	}
}
