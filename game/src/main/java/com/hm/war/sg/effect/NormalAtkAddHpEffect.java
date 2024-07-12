package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.bear.RecoverBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

//普攻吸血
public class NormalAtkAddHpEffect extends BaseSkillEffect{
	
	public NormalAtkAddHpEffect() {
		super(SkillEffectType.NormalAtkAddHpEffect);
	}

	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk, Unit def, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		atk.addBear(frame,new RecoverBear(atk.getId(), getAddHp(atk, def, skill, skillFunction,hurtBear.getHurt()), hurtBear.getEffectFrame(),skillFunction.getId()));
	}
	
	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,long hurt) {
		double v = skillFunction.getFunctionValue(atk,def,skill);
		return (long)(hurt*v);
	}
	
}