package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class ManyHurtEffect extends BaseSkillEffect{
	private AtkAddType addType;
	
	public ManyHurtEffect(AtkAddType addType) {
		super(SkillEffectType.ManyHurt);
		this.addType = addType;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		long interval = skillFunction.getIntervalFrame();//伤害间隔
		long times = skillFunction.getContinuedFrame()/interval;//攻擊次數
		
		for (Unit def : unitList) {
			long hurt = getHurt(unit,def,skill,skillFunction);//伤害值
			long effectFrame = frame.getId() + skillFunction.getEffectFrame(unit,def);
			for (int i = 0; i < times; i++) {
				long startFrame = i*interval;
				HurtBear bear = new HurtBear(unit.getId(), startFrame+effectFrame, 
						hurt,skill.getId(),skillFunction.getId());
				bear.setAddType(addType.getType());
				def.addBear(frame,bear);
			}
		}
	}
	
	private long getHurt(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return (long)skillFunction.getFunctionValue(atk,def,skill);
	}
}