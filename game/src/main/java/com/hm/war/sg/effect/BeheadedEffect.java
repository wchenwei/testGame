package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.BeheadedBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;
/**
 * @Description: 斩杀
 * @author siyunlong  
 * @date 2018年10月12日 下午3:12:04 
 * @version V1.0
 */
public class BeheadedEffect extends BaseSkillEffect{
	
	public BeheadedEffect() {
		super(SkillEffectType.BeheadedEffect);
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
			int delayFrame = i*skillFunction.getDelayFrame();//延迟帧
			BeheadedBear bear = new BeheadedBear(unit.getId(), effectFrame+delayFrame,skill.getId(),skillFunction.getId());
			def.addBear(frame,bear);
		}
	}
	
}