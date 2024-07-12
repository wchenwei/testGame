package com.hm.war.sg.event;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AttrEvent extends BuffEvent{
	private int attrType;//属性类型
	private double value;//增加值
	
	public AttrEvent(Unit unit,TankAttrType attrType,long frame,double value,int funcId) {
		super(unit.getId(), UnitBufferType.AttrBuff, frame, funcId);
		this.attrType = attrType.getType();
		this.value = value;
	}

	@Override
	public String toString() {
		return this.funcId + "->增加属性:" + TankAttrType.getType(this.attrType).getDesc() + " :" + value;
	}
}
