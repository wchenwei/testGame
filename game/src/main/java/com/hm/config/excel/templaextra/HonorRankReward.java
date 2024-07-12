package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;

import java.util.List;

public class HonorRankReward {
	private List<ActivityHonorRankTemplate> rankList = Lists.newArrayList();
	private int maxRank;
	
	public void addRankTemplate(ActivityHonorRankTemplate e) {
		this.rankList.add(e);
	}
	
	public void calMaxRank() {
		this.maxRank = rankList.stream().mapToInt(e -> e.getRank_up()).max().getAsInt();
	}

	public int getMaxRank() {
		return maxRank;
	}
	
	public ActivityHonorRankTemplate getRankTemplate(int rank) {
		return this.rankList.stream().filter(e -> e.isFit(rank)).findAny().orElse(null);
	}
}
