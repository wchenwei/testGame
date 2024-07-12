package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 
 * @Description: 重置普工
 * @author siyunlong  
 * @date 2018年10月16日 下午2:42:09 
 * @version V1.0
 */
public class ResetNormalCDEffect extends BaseSkillEffect{
	
	public ResetNormalCDEffect() {
		super(SkillEffectType.ResetNormalCDEffect);
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		resetCD(unit);
	}
	@Override
	public void doAtkHurtBear(Frame frame, Unit unit,Unit def,HurtBear hurtBear,Skill skill, SkillFunction skillFunction){
		resetCD(unit);
	}

	@Override
	public void doDefHurtBear(Frame frame, Unit def, List<Unit> unitList, HurtBear hurtBear, Skill skill,
			SkillFunction skillFunction) {
		resetCD(def);
	}
	
	public void resetCD(Unit unit) {
		unit.getAtkEngine().updateLastFrame(0);
	}
	
}

