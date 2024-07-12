package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.ShieldBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.ShieldEvent;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 偷buff
 * @date 2024/1/6 14:15
 */
public class StealBuffEffect extends BaseSkillEffect{
	private int stealEffId;

	public StealBuffEffect(int stealEffId) {
		super(SkillEffectType.StealBuffEffect);
		this.stealEffId = stealEffId;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		for (Unit def : unitList) {
			List<BaseBuffer> bufferList = def.getUnitBuffs().removeBufferByEffectId(this.stealEffId);
			//清除别人
			ClearBuffEffect.clearBuff(frame,skillFunction.getId(),def,bufferList);
			//添加给自己
			for (BaseBuffer buffer : bufferList) {
				stealBuff(frame,unit,skillFunction,buffer);
			}
		}
	}

	public void stealBuff(Frame frame, Unit unit,SkillFunction skillFunction,BaseBuffer buffer) {
		if(!unit.getUnitBuffs().replaceSkillBuff(frame, buffer)) {
			return;
		}
		unit.addBuffer(buffer);
		frame.addEvent(createEvent(frame,unit,buffer));
	}
	public Event createEvent(Frame frame, Unit unit, BaseBuffer buffer) {
		long continueFrame = buffer.getEndFrame()-frame.getId();
		if(continueFrame <= 0) {
			return null;
		}
		if(buffer.getType() != UnitBufferType.ShieldBuff) {
			return null;
		}
		ShieldBuff shieldBuff = (ShieldBuff) buffer;
		return new ShieldEvent(unit.getId(), continueFrame, shieldBuff.getShieldValue(), shieldBuff.getFuncId());
	}
}