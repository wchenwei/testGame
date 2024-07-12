package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitAtkAction;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

public class CritEffect extends BaseSkillEffect{
	
	public CritEffect() {
		super(SkillEffectType.Crit);
	}

	/**
	 * 攻击时触发
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAtkHurtBear(Frame frame, Unit atk, Unit def, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		if(hurtBear.getAddType() != AtkAddType.Crit.getType()) {
			//触发暴击
			HurtBear newBear = UnitAtkAction.calNormalAtkHurt(frame, atk, def, true);
			hurtBear.setHurt(newBear.getHurt());
			hurtBear.setAtkType(AtkAddType.Crit);
		}
	}
}
