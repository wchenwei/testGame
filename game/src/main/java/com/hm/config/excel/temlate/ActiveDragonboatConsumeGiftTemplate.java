package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_dragonBoat_consume_gift")
public class ActiveDragonboatConsumeGiftTemplate {
	private Integer id;
	private Integer stage;
	private Integer server_level_low;
	private Integer server_level_high;
	private Integer gold;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getServer_level_low() {
		return server_level_low;
	}

	public void setServer_level_low(Integer server_level_low) {
		this.server_level_low = server_level_low;
	}
	public Integer getServer_level_high() {
		return server_level_high;
	}

	public void setServer_level_high(Integer server_level_high) {
		this.server_level_high = server_level_high;
	}
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
