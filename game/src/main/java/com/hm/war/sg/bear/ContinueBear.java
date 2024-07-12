package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.BuffKind;
import com.hm.war.sg.buff.ContinueBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.BuffEvent;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;
import lombok.Setter;

@Setter
public class ContinueBear extends Bear{
	private int interval;
	private long count;//次数

	private long addHp;
	private long addMp;
	private int funcId;
	private BuffKind buffKind;
	private long maxBuffOvers;//最大叠加层数
	private SkillFunction skillFunction;
	private boolean isEveryCal;//是否每次都计算伤害
	private Unit buffUnit;

	public ContinueBear(Unit buffUnit, long addHp, long addMp, long effectFrame, SkillFunction skillFunction) {
		super(buffUnit.getId(), effectFrame);
		this.interval = skillFunction.getIntervalFrame();
		long continueFrame = skillFunction.getContinuedFrame();
		if(continueFrame > 0) {
			this.count = (int)(continueFrame/interval);
		}else{
			this.count = -1;
		}
		this.skillFunction = skillFunction;
		this.addHp = addHp;
		this.addMp = addMp;
		this.funcId = skillFunction.getId();
		this.buffKind = skillFunction.getBuffKind();
		this.maxBuffOvers = skillFunction.getMaxAttBuffOvers();
		this.buffUnit = buffUnit;
	}

	@Override
	public Event createEvent(Unit unit) {
//		return new ContinueEvent(unit, interval*count, addHp, addMp, funcId);
		return new BuffEvent(unit.getId(), UnitBufferType.ContinueBuff, interval*count, funcId);
	}

	@Override
	public boolean doUnit(Frame frame,Unit unit) {
		//添加持续buff,判断最大层数
		unit.getUnitBuffs().resetSkillBuffOverTimes(funcId, this.maxBuffOvers,UnitBufferType.ContinueBuff);
		ContinueBuff continueBuff = new ContinueBuff(buffUnit,addHp, addMp, getEffectFrame(), interval, count, funcId,buffKind);
		if (skillFunction != null) {
			continueBuff.setDelBuffType(skillFunction.getDelBuffType());
			if(isEveryCal) {
				continueBuff.setSkillFunction(skillFunction);
			}
		}
		continueBuff.setBuffUnitId(getAtkId());
		unit.addBuffer(continueBuff);
		return true;
	}

}
