package com.hm.libcore.leaderboards;

public class ResetRankToScore extends LeaderboardGetRank{
	private int score;
	
	public ResetRankToScore(String game, String name,int score) {
		super(game, name);
		this.score = score;
	}
	
}
