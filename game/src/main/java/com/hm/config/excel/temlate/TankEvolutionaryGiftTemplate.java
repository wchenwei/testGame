package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_evolutionary_gift")
public class TankEvolutionaryGiftTemplate {
	private Integer tank_id;
	private String recharge_gift_id;
	private int server_lv;
	private int limit_buy;

	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public String getRecharge_gift_id() {
		return recharge_gift_id;
	}

	public void setRecharge_gift_id(String recharge_gift_id) {
		this.recharge_gift_id = recharge_gift_id;
	}
	public int getServer_lv() {
		return server_lv;
	}

	public void setServer_lv(int server_lv) {
		this.server_lv = server_lv;
	}
	public int getLimit_buy() {
		return limit_buy;
	}

	public void setLimit_buy(int limit_buy) {
		this.limit_buy = limit_buy;
	}
}
