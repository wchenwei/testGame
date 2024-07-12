package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.unit.Unit;

/**
 * @Description: 没有buf触发
 * @author siyunlong  
 * @date 2018年12月24日 下午3:14:06 
 * @version V1.0
 */
public class NoneBuffTrigger extends BaseTriggerSkill{
	private UnitBufferType buffType;
	private boolean isAtk;//是否是攻击者
	
	public NoneBuffTrigger(boolean isAtk) {
		super(SkillTriggerType.DefBuff);
		this.isAtk = isAtk;
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		return isAtk ? haveNoBuff(atk,def) : haveNoBuff(def,atk);
	}
	
	private boolean haveNoBuff(Unit unit,Unit send) {
		if(buffType == UnitBufferType.MarkBuff) {
			return unit.getUnitBuffs().getNotConditionBuffList().stream()
					.noneMatch(e -> e.getType() == this.buffType && e.getBuffSendId() == send.getId());
		}
		return unit.getUnitBuffs().getNotConditionBuffList().stream().noneMatch(e -> e.getType() == buffType);
	}
	
	@Override
	public void init(String parms,int lv) {
		this.buffType = UnitBufferType.getType(Integer.parseInt(parms.split(":")[0]));
	}
	
}
