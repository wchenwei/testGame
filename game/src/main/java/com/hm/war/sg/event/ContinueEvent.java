package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@Deprecated
@NoArgsConstructor
public class ContinueEvent extends Event{
	private long addHp;//回复/消耗血量
	private long addMp;//回复/消耗蓝量
	private int funcId;//
	private long frame;//持续帧数
	
	private long hp;//当前hp
	private double mp;//当前mp
	
	public ContinueEvent(Unit unit,long frame,long addHp,long addMp,int funcId) {
		super(unit.getId(), EventType.ContineEvent);
		this.addHp = addHp;
		this.addMp = addMp;
		this.funcId = funcId;
		this.frame = frame;
		this.hp = Math.max(unit.getHpEngine().getHp(), 0);
		this.mp = unit.getMpEngine().getMp();
	}
	
	@Override
	public String toString() {
		return "持续回复: "+funcId+" -> "+ addHp;
	}
}
