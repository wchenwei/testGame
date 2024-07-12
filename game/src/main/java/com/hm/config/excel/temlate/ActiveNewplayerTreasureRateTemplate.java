package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_newplayer_treasure_rate")
public class ActiveNewplayerTreasureRateTemplate {
	private Integer id;
	private Integer recharge_need;
	private Integer gold_need;
	private Float rate_down;
	private Float rate_up;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecharge_need() {
		return recharge_need;
	}

	public void setRecharge_need(Integer recharge_need) {
		this.recharge_need = recharge_need;
	}

	public Integer getGold_need() {
		return gold_need;
	}

	public void setGold_need(Integer gold_need) {
		this.gold_need = gold_need;
	}

	public Float getRate_down() {
		return rate_down;
	}

	public void setRate_down(Float rate_down) {
		this.rate_down = rate_down;
	}

	public Float getRate_up() {
		return rate_up;
	}

	public void setRate_up(Float rate_up) {
		this.rate_up = rate_up;
	}

}
