package com.hm.war.sg.effect;

import java.util.List;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.ContinueBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class ContinueRecoverHpEffect extends BaseSkillEffect{
	private boolean isAdd;

	public ContinueRecoverHpEffect(boolean isAdd) {
		super(SkillEffectType.RecoverHp);
		this.isAdd = isAdd;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList,Skill skill,SkillFunction skillFunction) {
		long effectFrame = frame.getId()+skillFunction.getEffectFrame();

		unitList.forEach(myUnit -> {
			long addHp = getAddHp(unit, myUnit, skill, skillFunction);
			if(addHp == 0) {
				return;
			}
			ContinueBear bear = new ContinueBear(unit, addHp, 0, effectFrame, skillFunction);
			myUnit.addBear(frame, bear);
		});
	}

	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		long addHp = getAddHp(atk, def, skill, skillFunction,hurtBear.getHurt());
		ContinueBear bear = new ContinueBear(atk, addHp, 0, hurtBear.getEffectFrame(), skillFunction);
		def.addBear(frame,bear);
	}

	@Override
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		long effectFrame = hurtBear.getEffectFrame();
		unitList.forEach(myUnit -> {
			long addHp = getAddHp(unit, myUnit, skill, skillFunction);
			if(addHp == 0) {
				return;
			}
			ContinueBear bear = new ContinueBear(unit, addHp, 0, effectFrame, skillFunction);
			myUnit.addBear(frame, bear);
		});
	}

	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		long v = (long)skillFunction.getFunctionValue(atk,def,skill,args);
		return isAdd?v:-v;
	}

}
