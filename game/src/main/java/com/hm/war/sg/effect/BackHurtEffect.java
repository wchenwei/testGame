package com.hm.war.sg.effect;

import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 
 * @Description: 反伤
 * @author siyunlong  
 * @date 2018年11月7日 上午11:25:10 
 * @version V1.0
 */
public class BackHurtEffect extends BaseSkillEffect{
	
	public BackHurtEffect() {
		super(SkillEffectType.BackHurtEffect);
	}
	
	@Override
	public void doDefHurtBear(Frame frame, Unit def, List<Unit> unitList, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		if(!def.getUnitBuffs().isCanBackHurt()) {
			return;//不能反伤
		}
		double backRate = getReduceHurt(def, def, skill, skillFunction);
		long backHurt = MathUtils.mul(hurtBear.getHurt(), backRate);
		final long trueHurt = Math.min(def.getHp(), backHurt);//取血量最小值
		//反伤
		unitList.forEach(atk -> {
			atk.addBear(frame, new HurtBear(def.getId(), hurtBear.getEffectFrame(), trueHurt, skill.getId(), skillFunction.getId()));
		});
	}
	
	public double getReduceHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
}