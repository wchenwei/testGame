package com.hm.war.sg.effect;

import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class AddHurtEffect extends BaseSkillEffect{
	
	public AddHurtEffect() {
		super(SkillEffectType.AddHurt);
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk, Unit def, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		double addRate = getAddHurt(atk, def, skill, skillFunction, hurtBear.getHurt());
		long reudceHurt = MathUtils.mul(hurtBear.getHurt(), addRate);
		hurtBear.setHurt(Math.max(0, hurtBear.getHurt()+reudceHurt));
	}
	
	public double getAddHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		return skillFunction.getFunctionValue(atk,def,skill,args);
	}
}