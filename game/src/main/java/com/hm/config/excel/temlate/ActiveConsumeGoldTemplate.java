package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_consume_gold")
public class ActiveConsumeGoldTemplate {
	private Integer index;
	private Integer cost;
	private Integer gold_per_hit;
	private Integer gold_total;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getGold_per_hit() {
		return gold_per_hit;
	}

	public void setGold_per_hit(Integer gold_per_hit) {
		this.gold_per_hit = gold_per_hit;
	}
	public Integer getGold_total() {
		return gold_total;
	}

	public void setGold_total(Integer gold_total) {
		this.gold_total = gold_total;
	}
}
