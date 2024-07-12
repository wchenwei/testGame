package com.hm.war.sg.event;

import com.hm.war.sg.buff.UnitBufferType;
import lombok.NoArgsConstructor;

/**
 * @Description: 护盾事件
 * @author siyunlong  
 * @date 2018年9月10日 下午3:55:39 
 * @version V1.0
 */
@NoArgsConstructor
public class ShieldEvent extends BuffEvent{
	private long value;//护盾值
	
	public ShieldEvent(int id, long frame,long value, int funcId) {
		super(id, UnitBufferType.ShieldBuff, frame, funcId);
		this.value = value;
	}

	@Override
	public String toString() {
		return "增加护盾:" + UnitBufferType.getType(buffType).getDesc() + " :  funcId:" + funcId + " 持续:" + frame + " 护盾:" + value;
	}
}
