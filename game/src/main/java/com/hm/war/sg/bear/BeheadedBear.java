package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.HurtEvent;
import com.hm.war.sg.unit.Unit;

public class BeheadedBear extends Bear{
	private int addType;
	private long skillId;//所受技能id 如果=0 就是普通攻击 >0 是技能攻击
	private int funcId;
	private int atkTimes = 1;//普攻子弹次数
	
	
	public BeheadedBear(int atkId, long endFrame,long skillId,int funcId) {
		super(atkId, endFrame);
		this.skillId = skillId;
		this.funcId = funcId;
	}

	@Override
	public Event createEvent(Unit unit) {
		long hurt = unit.getHp();
		return new HurtEvent(unit, hurt, addType,getAtkId(),skillId,funcId,atkTimes);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		unit.getHpEngine().doBeheaderKill(frame,getAtkId());
		return true;
	}
	
}
