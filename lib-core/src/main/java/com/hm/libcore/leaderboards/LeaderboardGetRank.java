package com.hm.libcore.leaderboards;

/**   
 * @Description: 
 * @author siyunlong  
 * @date 2016年8月17日 下午2:25:51 
 * @version V1.0   
 */
public class LeaderboardGetRank {
	protected String Name;
	protected String Game;
	
	public LeaderboardGetRank(String game, String name) {
		this.Game = game;
		this.Name = name;
	}
}
