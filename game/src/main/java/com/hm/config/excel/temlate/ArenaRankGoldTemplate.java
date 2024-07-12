package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("arena_rank_gold")
public class ArenaRankGoldTemplate {
	private Integer id;
	private Integer rank;
	private String gold;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}
}
