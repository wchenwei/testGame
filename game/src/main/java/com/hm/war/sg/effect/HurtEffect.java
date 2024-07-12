package com.hm.war.sg.effect;

import cn.hutool.core.convert.Convert;
import com.hm.util.RandomUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class HurtEffect extends BaseSkillEffect{
	private AtkAddType addType;
	//伤害无视护盾
	private boolean ignoreShield;
	
	public HurtEffect(AtkAddType addType) {
		super(SkillEffectType.Hurt);
		this.addType = addType;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
			int delayFrame = i*skillFunction.getDelayFrame();//延迟帧
			HurtBear bear = new HurtBear(unit.getId(), effectFrame+delayFrame, 
					getHurt(unit,def,skill,skillFunction),skill.getId(),skillFunction.getId());
			bear.setAddType(addType.getType());
			bear.setIgnoreShield(rateIsIgnoreShield(skillFunction));
			def.addBear(frame,bear);
		}
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		long addHurt = getHurt(atk,def,skill,skillFunction);
		hurtBear.setHurt(hurtBear.getHurt()+addHurt);
	}
	
	private long getHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return (long)skillFunction.getFunctionValue(atk,def,skill);
	}

	public HurtEffect setIgnoreShield(boolean ignoreShield) {
		this.ignoreShield = ignoreShield;
		return this;
	}


	private boolean rateIsIgnoreShield(SkillFunction skillFunction) {
		if(!this.ignoreShield) {
			return false;
		}
		double rate = Convert.toDouble(skillFunction.getExtra_param());
		return RandomUtils.randomIsRate(rate);
	}
}
