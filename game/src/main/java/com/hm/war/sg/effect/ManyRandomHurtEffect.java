package com.hm.war.sg.effect;

import com.google.common.collect.Maps;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.Map;

/**
 * 
 * @Description: 多次伤害，按照敌方单位 顺序伤害
 * @author siyunlong  
 * @date 2018年11月9日 下午6:03:13 
 * @version V1.0
 */
public class ManyRandomHurtEffect extends BaseSkillEffect{
	public ManyRandomHurtEffect() {
		super(SkillEffectType.ManyHurt);
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		Map<Unit,Integer> randomMap = Maps.newHashMap();
		for (Unit def : unitList) {
			randomMap.put(def, randomMap.getOrDefault(def, 0)+1);
		}
		for (Unit def: randomMap.keySet()) {
			long hurt = getHurt(unit,def,skill,skillFunction);//伤害值
			long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
			for (int i = 0; i < randomMap.get(def); i++) {
				long startFrame = i;
				HurtBear bear = new HurtBear(unit.getId(), startFrame+effectFrame, 
						hurt,skill.getId(),skillFunction.getId());
				def.addBear(frame,bear);
			}
		}
	}
	
	private long getHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return (long)skillFunction.getFunctionValue(atk,def,skill);
	}
}