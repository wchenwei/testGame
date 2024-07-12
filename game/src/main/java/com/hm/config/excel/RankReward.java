package com.hm.config.excel;

import com.google.common.collect.Lists;
import com.hm.config.excel.templaextra.RankTemplate;

import java.util.List;

public class RankReward {
	private List<RankTemplate> rankList = Lists.newArrayList();
	private int maxRank;
	private int serverLv;
	
	public RankReward(int serverLv) {
		super();
		this.serverLv = serverLv;
	}

	public void addRankTemplate(RankTemplate e) {
		this.rankList.add(e);
	}
	
	public void calMaxRank() {
		this.maxRank = rankList.stream().mapToInt(e -> e.getRank_up()).max().getAsInt();
	}

	public int getMaxRank() {
		return maxRank;
	}
	
	public RankTemplate getRankTemplate(int rank) {
		return this.rankList.stream().filter(e -> e.isFit(rank)).findAny().orElse(null);
	}

	public int getServerLv() {
		return serverLv;
	}
}
