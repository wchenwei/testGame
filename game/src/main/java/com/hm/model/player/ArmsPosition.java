package com.hm.model.player;

import com.hm.config.GameConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArmsPosition {
	
	private int id;//图纸id
	private Arms arms;
	private long endTime;//建造时间
	private int state;//0-已分配到此处 1-建造中 2-上阵
	
	public ArmsPosition(int paperId) {
		this.id = paperId;
	}

	public void buildStart(int minutes) {
		this.state = 1;
		this.endTime = System.currentTimeMillis()+minutes*GameConstants.MINUTE;
	}

	public ArmsPosition(Arms arms) {
		this.id = 0;//把图纸变成0
		this.arms = arms;
		this.state = 2;
	}

	public void up(int index, Arms arms) {
		this.arms = arms;
	}
	
}
