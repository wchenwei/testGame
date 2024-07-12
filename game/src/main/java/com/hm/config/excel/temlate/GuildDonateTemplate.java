package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_donate")
public class GuildDonateTemplate {
	private Integer id;
	private Integer type;
	private Integer count;
	private String cost;
	private String reward;
	private Integer guild_exp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	public Integer getGuild_exp() {
		return guild_exp;
	}

	public void setGuild_exp(Integer guild_exp) {
		this.guild_exp = guild_exp;
	}
}
