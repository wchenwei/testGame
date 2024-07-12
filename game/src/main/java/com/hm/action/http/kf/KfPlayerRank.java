package com.hm.action.http.kf;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KfPlayerRank {
	private int id;
	private int rank;
	
	public KfPlayerRank(int id, int rank) {
		super();
		this.id = id;
		this.rank = rank;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
}
