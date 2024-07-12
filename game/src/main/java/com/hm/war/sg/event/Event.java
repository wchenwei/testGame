package com.hm.war.sg.event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Event {
	int id;//所属者
	int type;//类型


	public Event(int id,EventType type) {
		this.id = id;
		this.type = type.getType();
	}
	
	public void setType(int type) {
		this.type = type;
	}


	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}
	
	public void showHp() {
		
	}
}
