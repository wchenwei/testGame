package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@Deprecated
@NoArgsConstructor
public class StateSyncEvent extends Event{
	private long hp;
	private double mp;
	
	public StateSyncEvent(Unit unit) {
		super(unit.getId(), EventType.StateSync);
		this.hp = unit.getHpEngine().getHp();
		this.mp = unit.getMpEngine().getMp();
	}
	
}