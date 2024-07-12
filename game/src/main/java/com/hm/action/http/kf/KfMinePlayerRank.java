package com.hm.action.http.kf;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KfMinePlayerRank {
	private int id;
	private int rank;
	private long[] values = new long[2];
	
	public KfMinePlayerRank(int id, int rank, long[] values) {
		super();
		this.id = id;
		this.rank = rank;
		this.values = values;
	}
}
