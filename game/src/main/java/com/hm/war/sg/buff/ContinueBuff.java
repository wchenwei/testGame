package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.HurtEvent;
import com.hm.war.sg.event.RecoverEvent;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;
import lombok.Setter;

@Setter
public class ContinueBuff extends BaseBuffer{
	private int interval;
	private long count;//次数
	private long nextEffectFrame;

	private long addHp;
	private long addMp;
	private Unit buffUnit;
	private SkillFunction skillFunction;

	public ContinueBuff(Unit buffUnit,long addHp, long addMp, long effectFrame,int interval,long count,int funcId,BuffKind buffKind) {
		super(UnitBufferType.ContinueBuff,-1,buffKind);
		this.nextEffectFrame = effectFrame;
		this.interval = interval;
		this.count = count;
		this.addHp = addHp;
		this.addMp = addMp;
		this.funcId = funcId;
		this.buffUnit = buffUnit;
	}

	@Override
	public boolean isOver(long curFrame) {
		return count == 0;
	}

	@Override
	public void doEffectBuff(Frame frame,Unit unit) {
		if(frame.getId() >= this.nextEffectFrame) {
			this.count --;
			this.nextEffectFrame += interval;

			long buffVal = getBuffVal(unit);

			if(buffVal < 0) {
				unit.getHpEngine().reduceHp(frame, buffUnit.getId(), -buffVal,true,this.funcId,false);
			}else{
				unit.getHpEngine().addHp(buffVal);
			}
			unit.getMpEngine().addMpForSkill(frame,this.addMp);
			//不绑定特效
			if(buffVal < 0) {
				//有护盾和无敌 变成格挡
				int addType = unit.getUnitBuffs().haveWardOffBuff(false)?AtkAddType.WardOff.getType():0;
				long hurt = -buffVal;
				HurtEvent event = new HurtEvent(unit, hurt, addType,0,0,funcId,1);
				frame.addEvent(event);
			}else {
				double addTrueMp = (long)unit.getMpEngine().calTrueAddMp(this.addMp);
				if(addTrueMp > 0) {
					Event event = new RecoverEvent(unit, buffVal,addTrueMp,this.funcId);
					frame.addEvent(event);
				}
			}
		}
	}

	public long getBuffVal(Unit def) {
		if(this.skillFunction == null) {
			return this.addHp;
		}
		long v = (long)skillFunction.getFunctionValue(buffUnit,def,skillFunction.getSkill());
		return this.addHp > 0?v:-v;
	}

}
