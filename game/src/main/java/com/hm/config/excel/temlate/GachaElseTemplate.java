package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("gacha_else")
public class GachaElseTemplate {
	private Integer index;
	private Integer max_level;
	private Integer reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getMax_level() {
		return max_level;
	}

	public void setMax_level(Integer max_level) {
		this.max_level = max_level;
	}
	public Integer getReward() {
		return reward;
	}

	public void setReward(Integer reward) {
		this.reward = reward;
	}
}
