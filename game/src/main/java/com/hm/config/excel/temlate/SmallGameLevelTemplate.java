package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("small_game_level")
public class SmallGameLevelTemplate {
	private Integer level;
	private Integer exp;
	private Integer exp_total;
	private String reward_show;
	private Integer maxexp;
	private Integer maxpoint;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getExp_total() {
		return exp_total;
	}

	public void setExp_total(Integer exp_total) {
		this.exp_total = exp_total;
	}
	public String getReward_show() {
		return reward_show;
	}

	public void setReward_show(String reward_show) {
		this.reward_show = reward_show;
	}
	public Integer getMaxexp() {
		return maxexp;
	}

	public void setMaxexp(Integer maxexp) {
		this.maxexp = maxexp;
	}
	public Integer getMaxpoint() {
		return maxpoint;
	}

	public void setMaxpoint(Integer maxpoint) {
		this.maxpoint = maxpoint;
	}
}
