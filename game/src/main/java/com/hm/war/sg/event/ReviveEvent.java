package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReviveEvent extends Event{
	private long hp;
	private double mp;
	private int funcId;
	
	public ReviveEvent(Unit unit,int funcId) {
		super(unit.getId(), EventType.ReviveEvent);
		this.hp = unit.getHpEngine().getHp();
		this.mp = unit.getMpEngine().getMp();
		this.funcId = funcId;
	}
	
	@Override
	public void showHp() {
		System.err.println("复活："+getId()+"="+this.hp);
	}

	@Override
	public String toString() {
		return "复活："+getId()+"="+this.hp;
	}
}
