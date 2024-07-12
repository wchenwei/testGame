package com.hm.war.sg;

import com.google.common.collect.Lists;
import com.hm.war.sg.event.Event;
import lombok.Data;

import java.util.List;

@Data
public class Frame {
	private long id;//帧数
	//帧事件列表
	private List<Event> eventList = Lists.newArrayList();
	
	public Frame(long id) {
		this.id = id;
	}
	
	public void addEvent(Event event) {
		if(event == null) {
			return;
		}
		this.eventList.add(event);
	}
	
	public boolean isEmptyEvent() {
		return this.eventList.isEmpty();
	}
}
