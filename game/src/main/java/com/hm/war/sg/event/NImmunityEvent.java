package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

/**
 * 通用免疫事件
 */
@NoArgsConstructor
public class NImmunityEvent extends Event{
	private int type;

	public NImmunityEvent(Unit unit) {
		super(unit.getId(), EventType.ImmunityEvent);
	}

	
	@Override
	public String toString() {
        return getId()+"通用免疫";
	}
	@Override
	public void showHp() {
		System.err.println(getId()+"通用免疫");
	}
}
