package com.hm.libcore.leaderboards;

import java.util.List;

import com.google.gson.Gson;

/**   
 * @Description: 
 * @author siyunlong  
 * @date 2016年8月17日 下午2:25:51 
 * @version V1.0   
 */
public class LeaderboardGetPlayerRankForList {
	protected String Game;
	protected String RankNames;
	protected String Name;
	
	public LeaderboardGetPlayerRankForList(String game,long playerId, List<String> RankNames) {
		this.Game = game;
		this.Name = playerId + "";
		this.RankNames = new Gson().toJson(RankNames);
	}
	
}
