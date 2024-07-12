package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("gacha_reward")
public class GachaRewardTemplate {
	private Integer index;
	private String nomal;
	private String adv_once_notget;
	private String adv_once_gettank;
	private String adv_ten_notget;
	private String adv_ten_gettank;
	private String ss_tank_rate;
	private String s_tank_rate;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getNomal() {
		return nomal;
	}

	public void setNomal(String nomal) {
		this.nomal = nomal;
	}
	public String getAdv_once_notget() {
		return adv_once_notget;
	}

	public void setAdv_once_notget(String adv_once_notget) {
		this.adv_once_notget = adv_once_notget;
	}
	public String getAdv_once_gettank() {
		return adv_once_gettank;
	}

	public void setAdv_once_gettank(String adv_once_gettank) {
		this.adv_once_gettank = adv_once_gettank;
	}
	public String getAdv_ten_notget() {
		return adv_ten_notget;
	}

	public void setAdv_ten_notget(String adv_ten_notget) {
		this.adv_ten_notget = adv_ten_notget;
	}
	public String getAdv_ten_gettank() {
		return adv_ten_gettank;
	}

	public void setAdv_ten_gettank(String adv_ten_gettank) {
		this.adv_ten_gettank = adv_ten_gettank;
	}
	public String getSs_tank_rate() {
		return ss_tank_rate;
	}

	public void setSs_tank_rate(String ss_tank_rate) {
		this.ss_tank_rate = ss_tank_rate;
	}
	public String getS_tank_rate() {
		return s_tank_rate;
	}

	public void setS_tank_rate(String s_tank_rate) {
		this.s_tank_rate = s_tank_rate;
	}
}
