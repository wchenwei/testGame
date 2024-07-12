package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("camp_prisoner")
public class CampPrisonerTemplate {
	private Integer id;
	private String upgrade_cost;
	private Double chance_catch_npc;
	private Double chance_catch_player;
	private Integer catch_limit;
	private Integer research_auto;
	private String extra_reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getUpgrade_cost() {
		return upgrade_cost;
	}

	public void setUpgrade_cost(String upgrade_cost) {
		this.upgrade_cost = upgrade_cost;
	}

	public Double getChance_catch_npc() {
		return chance_catch_npc;
	}

	public void setChance_catch_npc(Double chance_catch_npc) {
		this.chance_catch_npc = chance_catch_npc;
	}

	public Double getChance_catch_player() {
		return chance_catch_player;
	}

	public void setChance_catch_player(Double chance_catch_player) {
		this.chance_catch_player = chance_catch_player;
	}

	public Integer getCatch_limit() {
		return catch_limit;
	}

	public void setCatch_limit(Integer catch_limit) {
		this.catch_limit = catch_limit;
	}
	public Integer getResearch_auto() {
		return research_auto;
	}

	public void setResearch_auto(Integer research_auto) {
		this.research_auto = research_auto;
	}
	public String getExtra_reward() {
		return extra_reward;
	}

	public void setExtra_reward(String extra_reward) {
		this.extra_reward = extra_reward;
	}
}
