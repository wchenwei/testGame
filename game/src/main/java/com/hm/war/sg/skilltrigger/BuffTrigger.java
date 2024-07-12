package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.unit.Unit;

public class BuffTrigger extends BaseTriggerSkill{
	private UnitBufferType buffType;
	private boolean isAtk;//是否是攻击者
	
	public BuffTrigger(boolean isAtk) {
		super(SkillTriggerType.DefBuff);
		this.isAtk = isAtk;
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		int sendId = args.length > 0 ?(int)args[0]:-1;
		if(sendId == -1) {
			sendId = atk.getId();
		}
		return isAtk ? haveBuff(atk,def,sendId) : haveBuff(def,atk,sendId);
	}
	
	private boolean haveBuff(Unit unit,Unit send,int sendId) {
		if(buffType == UnitBufferType.MarkBuff) {
			return unit.getUnitBuffs().getNotConditionBuffList().stream()
					.anyMatch(e -> e.getType() == this.buffType && e.getBuffSendId() == sendId);
		}
		return unit.getUnitBuffs().getNotConditionBuffList().stream().anyMatch(e -> e.getType() == buffType);
	}
	
	@Override
	public void init(String parms,int lv) {
		this.buffType = UnitBufferType.getType(Integer.parseInt(parms.split(":")[0]));
	}

	public UnitBufferType getBuffType() {
		return buffType;
	}
	
}
