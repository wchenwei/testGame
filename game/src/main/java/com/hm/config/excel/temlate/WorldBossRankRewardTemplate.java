package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("world_boss_rank_reward")
public class WorldBossRankRewardTemplate {
	private Integer index;
	private Integer boss_id_down;
	private Integer boss_id_up;
	private Integer rank_down;
	private Integer rank_up;
	private String reward;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getBoss_id_down() {
		return boss_id_down;
	}

	public void setBoss_id_down(Integer boss_id_down) {
		this.boss_id_down = boss_id_down;
	}
	public Integer getBoss_id_up() {
		return boss_id_up;
	}

	public void setBoss_id_up(Integer boss_id_up) {
		this.boss_id_up = boss_id_up;
	}
	public Integer getRank_down() {
		return rank_down;
	}

	public void setRank_down(Integer rank_down) {
		this.rank_down = rank_down;
	}
	public Integer getRank_up() {
		return rank_up;
	}

	public void setRank_up(Integer rank_up) {
		this.rank_up = rank_up;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
