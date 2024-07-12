package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

public class PlayerRepairTrain extends PlayerDataContext {
	private long score;//今日历史最高分
	private long lastScore;//最后一次获得积分
	
	public void updateScore(int score){
		this.lastScore = score;
		if(score>this.score){
			this.score=score;
		}
		SetChanged();
	}
	
	public long getScore() {
		return score;
	}

	public long getLastScore() {
		return lastScore;
	}
	public void resetDay(){
		this.score=0;
		this.lastScore=0;
		SetChanged();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerRepairTrain", this);
	}
}
