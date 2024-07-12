package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

public class TankTypeTrigger extends BaseTriggerSkill{
	private boolean isAtk;
	private int type;//坦克类型

	public TankTypeTrigger(boolean isAtk) {
		super(SkillTriggerType.AtkTankType);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		Unit temp = isAtk? atk:def;
		return temp.getSetting().getAmyType() == type;
	}
	
	public void init(String parms,int lv) {
		this.type = Integer.parseInt(parms);
	}
}
