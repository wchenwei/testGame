package com.hm.libcore.leaderboards;

public class RankRange {
	String name;
	int start;
	int end;
	
	public RankRange(String name, int start, int end) {
		this.name = name;
		this.start = start-1;
		this.end = end-1;
	}

	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	
	
}
