package com.hm.war.sg.buff;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.unit.Unit;

import java.util.Map;

public class AttrBuff extends BaseBuffer{
	protected TankAttrType attrType;//属性类型
	protected int unitId;//所属坦克
	protected int sendId;
	
	public AttrBuff(long endFrame,TankAttrType attrType,double value,int sendId,int unitId,int funcId,BuffKind buffKind) {
		super(UnitBufferType.AttrBuff,endFrame,buffKind);
		this.sendId = sendId;
		this.unitId = unitId;
		this.attrType = attrType;
		setFuncId(funcId);
		setValue(value);
	}
	
	@Override
	public int getBuffSendId() {
		return sendId;
	}
	
	@Override
	public boolean isBelongUnitForverBuff(int unitId) {
		return this.unitId == unitId && isForoverBuff();
	}
	
	public TankAttrType getAttrType() {
		return attrType;
	}

	@Override
	public void calBuffUnitAttr(Unit unit, Map<TankAttrType,Double> attrMap) {
		if(!isFitCondition(unit)) {
			return;
		}
		attrMap.put(attrType, attrMap.getOrDefault(attrType, 0d)+getValue());
	}
}
