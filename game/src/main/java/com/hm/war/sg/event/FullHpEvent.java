package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FullHpEvent extends Event{
	private int boosId;
	private long maxHp;

	public FullHpEvent(Unit unit, int boosId) {
		super(unit.getId(), EventType.FullHp);
		this.boosId = boosId;
		this.maxHp = unit.getSetting().getMaxHp();
	}
}
