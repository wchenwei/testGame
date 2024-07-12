package com.hm.war.sg.event;

import lombok.NoArgsConstructor;

/**
 * @Description:死亡事件
 * @author siyunlong  
 * @date 2018年11月3日 下午3:07:32 
 * @version V1.0
 */
@NoArgsConstructor
public class DeathEvent extends Event{
	public DeathEvent(int id) {
		super(id, EventType.DeathEevet);
	}

	@Override
	public String toString() {
		return "死亡";
	}
}
