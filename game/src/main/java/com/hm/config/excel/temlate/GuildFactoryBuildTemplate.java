package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_factory_build")
public class GuildFactoryBuildTemplate {
	private Integer id;
	private String item;
	private Integer exp_add;
	private Integer part_add;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}
	public Integer getExp_add() {
		return exp_add;
	}

	public void setExp_add(Integer exp_add) {
		this.exp_add = exp_add;
	}
	public Integer getPart_add() {
		return part_add;
	}

	public void setPart_add(Integer part_add) {
		this.part_add = part_add;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
