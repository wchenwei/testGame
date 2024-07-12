package com.hm.model.player;

import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CaptiveTankInfo {
	private int tankId;//已经俘虏的坦克id
	private long playerId;
	private String name;
	private long endTime;
	private int star; //星级
	//============================================
	private int tech;//研究次数
	
	public CaptiveTankInfo(UnitGroup loseGroup,Unit unit,long endTime) {
		super();
		this.tankId = unit.getSetting().getTankId();
		this.playerId = loseGroup.getPlayerId();
		this.name = loseGroup.getName();
		this.endTime = endTime;
		this.star = unit.getSetting().getUnitInfo().getStar();
	}
	
	public CaptiveTankInfo(int tankId, long playerId, String name, long endTime, int star) {
		super();
		this.tankId = tankId;
		this.playerId = playerId;
		this.name = name;
		this.endTime = endTime;
		this.star = star;
	}
	
	public boolean isFitTime() {
		return this.endTime > System.currentTimeMillis();
	}

	public boolean isDidTech(){
		return tech > 0;
	}

	public void doTech(){
		this.tech ++;
	}

	public boolean isPlayerTank(){
		return playerId > 0;
	}
}
