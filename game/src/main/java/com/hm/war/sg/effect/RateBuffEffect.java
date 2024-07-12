package com.hm.war.sg.effect;

import com.hm.util.RandomUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.BuffBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 概率添加buff
 * @author siyunlong  
 * @date 2018年11月14日 下午7:54:56 
 * @version V1.0
 */
public class RateBuffEffect extends BaseSkillEffect{
	private UnitBufferType buffType;
	
	public RateBuffEffect(UnitBufferType buffType) {
		super(SkillEffectType.Silent);
		this.buffType = buffType;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			long effectFrame = frame.getId();
			int delayFrame = i*skillFunction.getDelayFrame();
			long continuedFrame = skillFunction.getContinuedFrame();
			double rate = getValue(unit, def, skill, skillFunction);
			if(RandomUtils.randomIsRate(rate) && isCanAddBuff(frame,def)) {
				def.addBear(frame,new BuffBear(unit.getId(), buffType, continuedFrame,effectFrame+delayFrame,skillFunction));
			}
		}
	}
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		long effectFrame = hurtBear.getEffectFrame();
		long continuedFrame = skillFunction.getContinuedFrame();
		double rate = getValue(atk, def, skill, skillFunction);

		if(RandomUtils.randomIsRate(rate) && isCanAddBuff(frame,def)) {
			def.addBear(frame,new BuffBear(atk.getId(), buffType ,continuedFrame,effectFrame,skillFunction));
		}
	}
	
	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
	
	//判断
	private boolean isCanAddBuff(Frame frame,Unit unit) {
		return unit.getUnitBuffs().isCanAddBuff(frame,buffType);
	}
}
