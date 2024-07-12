package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class ShowEvent extends Event{

	public ShowEvent(Unit unit,EventType eventType) {
		super(unit.getId(), eventType);
	}

	
	@Override
	public String toString() {
        return getId()+"飘字";
	}
	@Override
	public void showHp() {
		System.err.println(getId()+"飘字");
	}
}
