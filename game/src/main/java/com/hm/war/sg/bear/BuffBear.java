package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;

import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.buff.*;
import com.hm.war.sg.event.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BuffBear extends Bear{
	protected UnitBufferType buffType;
	protected long continueFrame;//持续的帧数
	protected double value;
	protected SkillFunction skillFunction;
	private Object confValue;//配置数据

	public BuffBear(int atkId, UnitBufferType buffType ,long continueFrame,long effectFrame,SkillFunction skillFunction) {
		super(atkId, effectFrame);
		this.continueFrame = continueFrame;
		this.buffType = buffType;
		this.skillFunction = skillFunction;
	}

	@Override
	public Event createEvent(Unit unit) {
		if(UnitBufferType.isShieldBuff(buffType)) {
			return new ShieldEvent(unit.getId(), this.continueFrame, (long)value, skillFunction.getId());
		}
		return new BuffEvent(unit.getId(), buffType,this.continueFrame,skillFunction.getId());
	}
	
	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		//重新计算持续时间
		if(continueFrame > 0 && buffType == UnitBufferType.StunBuff) {
			double reduceRate = unit.getUnitBuffs().getBuffSumValue(UnitBufferType.ReduceStunBuff);
			continueFrame = Math.max(0, (long)(continueFrame-reduceRate*continueFrame));
			if(continueFrame <= 0) {
				return false;
			}
		}
		BaseBuffer newBuff = createBuffer();
		if(newBuff == null) {
			return false;
		}
		newBuff.setSkillId(skillFunction.getSkill().getId());
		newBuff.setBuffUnitId(this.getAtkId());
		newBuff.setFuncId(skillFunction.getId());
		newBuff.setBuffKind(skillFunction.getBuffKind());
		newBuff.setBuffTriggerCondition(skillFunction.getBuffTriggerCondition());
		newBuff.setDelBuffType(skillFunction.getDelBuffType());
		newBuff.setEffectId(skillFunction.getEffectId());
		if(!unit.getUnitBuffs().replaceSkillBuff(frame, newBuff)) {
			return false;
		}
		unit.addBuffer(newBuff);
		if(newBuff.getType() == UnitBufferType.StunBuff) {
			frame.addEvent(new ShowEvent(unit, EventType.StunShow));
		}
		return true;
	}
	
	public BaseBuffer createBuffer() {
		long endFrame = continueFrame;
		if(endFrame > 0) {
			endFrame += getEffectFrame();
		}
		switch (buffType) {
			case StunBuff:
				return new StunBuff(endFrame);
			case SilentBuff:
				return new SilentBuff(endFrame);
			case NotStunBuff:
				return new NotStunBuff(endFrame);
			case ShieldBuff:
				return new ShieldBuff(endFrame, (long)value);
			case ShieldBuffBackHp:
				return new ShieldBuffBackHp(endFrame, (long)value,confValue);
			case ShieldBuffAddMp:
                return new ShieldBuffAddMp(endFrame, (long) value, confValue);
			case MarkBuff:
				return new MarkBuff(endFrame,getAtkId());
			case CureEffectBuff:
				return new CureEffectBuff(endFrame, value);
			case HurtEffectBuff:
				return new HurtEffectBuff(endFrame, value);
			case HurtHp10EffectBuff:
				return new HurtHp10EffectBuff(endFrame, value);
			case HurtHp80EffectBuff:
				return new HurtHp80EffectBuff(endFrame, value);
			case NoDeath:
				return new NoDeathBuff(endFrame);
			case InvincibleBuff:
				return new InvincibleBuff(endFrame);
			case AtkSuckBlood:
				return new SuckBloodBuff(endFrame, value);
			case BackHurtBuff:
				return new BackHurtBuff(endFrame, value);
			case AddAtkTargets:
				return new AddAtkTargetsBuff(endFrame,value);
			case RateNotStunBuff:
			case RateDodgeBuff:
			case RateNoSilentBuff:
			case RateNoSkillHurt:
			case RateNoReduceMP:
			case RateNoFixedBody:
				return new RateBuff(buffType,endFrame,value);
			case TauntBuff:
				return new TauntBuff(endFrame,getAtkId());
			case NoAtkBuff:
				return new NoAtkBuff(endFrame);
			case NextDeathBuff:
				return new NextDeathBuff(endFrame);
			case SmallSkillSilentBuff:
				return new SmallSkillSilentBuff(endFrame);
			case RandomAddAtkTargets:
				return new RandomAddAtkTargetsBuff(endFrame,value,confValue);
			case DodgeBuff:
				return new DodgeBuff(endFrame,value);
			case NotAddMpBuff:
				return new NotAddMpBuff(endFrame);
			case NotAddMpSkillBuff:
				return new NotAddMpSkillBuff(endFrame);
			case HurtSelfLink:
				return new HurtSelfLinkBuff(endFrame,value,getAtkId());
			case ReduceMpBuff:
				return new ReduceMpBuff(endFrame,value,getAtkId());
			case ReduceStunBuff:
				return new ReduceStunBuff(endFrame,value,getAtkId());
			case AtkDodgeBuff:
				return new AtkDodgeBuff(endFrame,value,getAtkId());
			case FixedBodyBuff:
				return new FixedBodyBuff(endFrame);
			case NoShieldBuff:
				return new NotShieldBuff(endFrame);
			case TransHurtBuff:
				return new TransHurtBuff(endFrame, value, confValue);
			case DefCritResReduce:
				return new DefCritResReduceBuff(endFrame,value);
		}
		return null;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setConfValue(Object confValue) {
		this.confValue = confValue;
	}
	
}