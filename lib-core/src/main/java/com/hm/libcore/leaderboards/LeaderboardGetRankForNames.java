package com.hm.libcore.leaderboards;

import java.util.List;

import com.google.gson.Gson;

/**   
 * @Description: 
 * @author siyunlong  
 * @date 2016年8月17日 下午2:25:51 
 * @version V1.0   
 */
public class LeaderboardGetRankForNames {
	protected String Game;
	protected String RankRanges;
	
	public LeaderboardGetRankForNames(String game, List<RankRange> RankRanges) {
		this.Game = game;
		this.RankRanges = new Gson().toJson(RankRanges);
	}
	
}
