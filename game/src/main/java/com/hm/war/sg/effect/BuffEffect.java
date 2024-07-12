package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.BuffBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

//不带附加值的buff
public class BuffEffect extends BaseSkillEffect{
	private UnitBufferType buffType;
	
	public BuffEffect(UnitBufferType buffType) {
		super(SkillEffectType.Silent);
		this.buffType = buffType;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
			int delayFrame = i*skillFunction.getDelayFrame();
			if(isCanAddBuff(frame,def)) {
				def.addBear(frame,new BuffBear(unit.getId(), buffType, getFrame(unit, def, skill, skillFunction),effectFrame+delayFrame,skillFunction));
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
			long effectFrame = hurtBear.getEffectFrame();
			def.addBear(frame,new BuffBear(atk.getId(), buffType ,getFrame(atk,def,skill, skillFunction),effectFrame,skillFunction));
		}
	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			long effectFrame = hurtBear.getEffectFrame();
			int delayFrame = 0;
			if(isCanAddBuff(frame,def)) {
				def.addBear(frame,new BuffBear(unit.getId(), buffType, getFrame(unit, def, skill, skillFunction),effectFrame+delayFrame,skillFunction));
			}
		}
	}
	
	//持续帧
	public long getFrame(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getContinuedFrame();
	}
	
	//判断
	private boolean isCanAddBuff(Frame frame,Unit unit) {
		return unit.getUnitBuffs().isCanAddBuff(frame,buffType);
	}
}
