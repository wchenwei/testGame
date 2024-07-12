package com.hm.model.kf;

import lombok.Data;

@Data
public class PlayerValue {
	private long playerId;
	private int[] value = new int[2];
	private int score;
	
	public PlayerValue(long playerId,int type,int value,int score) {
		this.playerId = playerId;
		this.value[type] = value;
		this.score = score;
	}
	
	public void addValue(int type,int value, int score) {
		this.value[type] += value;
		this.score += score;
	}
	
	public void clear() {
		this.score = 0;
		for (int i = 0; i < value.length; i++) {
			value[i] = 0;
		}
	}
}
