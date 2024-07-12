package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.BuffBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class BuffValueEffect extends BaseSkillEffect{
	private UnitBufferType buffType;
	private boolean isAdd;
	private Object confValue;//配置数据
	
	public BuffValueEffect(UnitBufferType buffType,boolean isAdd) {
		super(SkillEffectType.Shield);
		this.buffType = buffType;
		this.isAdd = isAdd;
	}
	public BuffValueEffect(UnitBufferType buffType,boolean isAdd,Object confValue) {
		super(SkillEffectType.Shield);
		this.buffType = buffType;
		this.isAdd = isAdd;
		this.confValue = confValue;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		long effectFrame = frame.getId()+skillFunction.getEffectFrame();
		long continueFrame = skillFunction.getContinuedFrame();
		
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			int delayFrame = i*skillFunction.getDelayFrame();
			if(isCanAddBuff(frame,def)) {
				double value = getValue(unit, def, skill, skillFunction);
				BuffBear bear = new BuffBear(unit.getId(), buffType, continueFrame,effectFrame+delayFrame,skillFunction);
				bear.setValue(value);
				bear.setConfValue(confValue);
				def.addBear(frame,bear);
			}
		}
	}
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	@Override
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		if(isCanAddBuff(frame,def)) {
			long continueFrame = skillFunction.getContinuedFrame();
			long effectFrame = hurtBear.getEffectFrame();
			double value = getValue(atk, def, skill, skillFunction);
			
			BuffBear bear = new BuffBear(atk.getId(), buffType ,continueFrame,effectFrame,skillFunction);
			bear.setValue(value);
			bear.setConfValue(confValue);
			def.addBear(frame,bear);
		}
	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		long effectFrame = hurtBear.getEffectFrame();
		long continueFrame = skillFunction.getContinuedFrame();
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			int delayFrame = 0;
			if(isCanAddBuff(frame,def)) {
				double value = getValue(unit, def, skill, skillFunction);
				BuffBear bear = new BuffBear(unit.getId(), buffType, continueFrame,effectFrame+delayFrame,skillFunction);
				bear.setValue(value);
				bear.setConfValue(confValue);
				def.addBear(frame,bear);
			}
		}
	}
	
	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		double v = skillFunction.getFunctionValue(atk,def,skill);
		return isAdd?v:-v;
	}
	
	//判断
	private boolean isCanAddBuff(Frame frame,Unit unit) {
		return unit.getUnitBuffs().isCanAddBuff(frame,buffType);
	}
}
