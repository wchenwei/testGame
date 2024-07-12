package com.hm.model.activity.kfseason.server;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ServerSeasonKfItem {
	private int score;
	private int val;
	
	public void addScore(int add,boolean isWin) {
		this.score += add;
		if(isWin) {
			this.val ++;
		}
	}
}
