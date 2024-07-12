package com.hm.war.sg.event;

import com.hm.war.sg.buff.UnitBufferType;
import lombok.NoArgsConstructor;

/**
 * @Description: buff事件
 * @author siyunlong  
 * @date 2018年9月10日 下午3:55:39 
 * @version V1.0
 */
@NoArgsConstructor
public class BuffEvent extends Event{
	public int buffType;
	public long frame;//持续帧数
	public int funcId;
	
	public BuffEvent(int id, UnitBufferType buffType,long frame,int funcId) {
		super(id, EventType.BuffEvent);
		this.frame = frame;
		this.funcId = funcId;
		this.buffType = buffType.getType();
	}

	@Override
	public String toString() {
		return "增加buff:" + UnitBufferType.getType(buffType).getDesc() + " :  funcId:" + funcId
				+ " 持续:" + frame ;
	}
}
