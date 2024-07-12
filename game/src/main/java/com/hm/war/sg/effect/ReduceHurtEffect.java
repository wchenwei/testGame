package com.hm.war.sg.effect;

import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class ReduceHurtEffect extends BaseSkillEffect{
	
	public ReduceHurtEffect() {
		super(SkillEffectType.ReduceHurt);
	}
	
	/**
	 * 受伤时触发减伤
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	@Override
	public void doDefHurtBear(Frame frame, Unit def, List<Unit> unitList, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		double reudceRate = getReduceHurt(def, def, skill, skillFunction, hurtBear.getHurt());
		long reudceHurt = MathUtils.mul(hurtBear.getHurt(), reudceRate);
		hurtBear.setHurt(Math.max(0, hurtBear.getHurt()-reudceHurt));
	}
	
	public double getReduceHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		return skillFunction.getFunctionValue(atk,def,skill,args);
	}
}