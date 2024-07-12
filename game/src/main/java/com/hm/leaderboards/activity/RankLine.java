package com.hm.leaderboards.activity;

import com.hm.leaderboards.LeaderboardInfo;
import com.hm.model.player.Player;

public class RankLine implements Comparable<RankLine> {
	private int rank;
	private long id;
	private String name;
	private long v1;
	private transient long v2; 

	public RankLine(int id, long v1, long v2) {
		this.id = id;
		this.v1 = v1;
		this.v2 = v2;
	}
	public RankLine(Player player, long v) {
		this.id = player.getId();
		this.v1 = v;
		this.v2 = System.currentTimeMillis();
		this.name = player.getName();
	}
	public RankLine(LeaderboardInfo leaderboardInfo) {
		this.id = leaderboardInfo.getIntId();
//		this.name = leaderboardInfo.getName();
		this.v1 = (long)leaderboardInfo.getScore();
		this.rank = leaderboardInfo.getRank();
	}

	public RankLine() {
		super();
	}
	
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "RankLine [id=" + id + ", v1=" + v1 + ", v2=" + v2 + "]";
	}

	@Override
	public int compareTo(RankLine temp) {
		if (temp == null) {
            return -1;
        }  
		if (this == temp) {
            return 0;  
        }  
        if(this.v1 == temp.v1) {
        	return (int)(temp.v2-this.v2);
        }
        return (int)(this.v1-temp.v1);
	}
	public String getName() {
		return name;
	}
	public long getV1() {
		return v1;
	}
	public long getV2() {
		return v2;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}
