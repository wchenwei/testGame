package com.hm.war.sg.event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AircraftEvent extends Event{
	private int index;//当前运行到飞机位置
	
	public AircraftEvent(int id,int index) {
		super(id, EventType.AircraftEvent);
		this.index = index;
	}
}
