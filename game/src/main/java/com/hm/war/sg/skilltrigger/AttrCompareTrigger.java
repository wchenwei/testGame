package com.hm.war.sg.skilltrigger;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class AttrCompareTrigger extends BaseTriggerSkill{
	private TankAttrType attrType;
	private boolean isLow;
	
	public AttrCompareTrigger(TankAttrType attrType,boolean isLow) {
		super(SkillTriggerType.AttrCompare);
		this.attrType = attrType;
		this.isLow = isLow;
	}

	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		if(isLow) {
			return def.getUnitAttr().getDoubleValue(attrType) < atk.getUnitAttr().getDoubleValue(attrType);
		}else{
			return def.getUnitAttr().getDoubleValue(attrType) >= atk.getUnitAttr().getDoubleValue(attrType);
		}
	}
		
}
