package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.bear.RecoverBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class AddHpEffect extends BaseSkillEffect{
	
	public AddHpEffect() {
		super(SkillEffectType.RecoverHp);
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		unitList.forEach(myUnit -> {
			long addHp = getAddHp(unit, myUnit, skill, skillFunction);
			RecoverBear bear = new RecoverBear(unit.getId(), addHp, frame.getId()+skillFunction.getEffectFrame(),skillFunction.getId());
			myUnit.addBear(frame,bear);
		});
	}
	
	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
//	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
//		def.addBear(frame,new RecoverBear(atk.getId(), getAddHp(atk, def, skill, skillFunction,hurtBear.getHurt()), hurtBear.getEffectFrame(),skillFunction.getId()));
//	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		unitList.forEach(myUnit -> {
			long addHp = getAddHp(unit, myUnit, skill, skillFunction);
			RecoverBear bear = new RecoverBear(unit.getId(), addHp, hurtBear.getEffectFrame(),skillFunction.getId());
			myUnit.addBear(frame,bear);
		});
	}
	
	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		//计算治疗百分比
		double addRate = def.getUnitBuffs().getBuffSumValue(UnitBufferType.CureEffectBuff);
		return (long)(skillFunction.getFunctionValue(atk,def,skill,args)*(1+addRate));
	}
	
}
