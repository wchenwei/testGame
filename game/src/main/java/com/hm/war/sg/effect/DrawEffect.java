package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.DrawBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class DrawEffect extends BaseSkillEffect{
	private int drawType;//0-吸血  1-吸蓝
	
	public DrawEffect(int type) {
		super(SkillEffectType.DrawEffect);
		this.drawType = type;
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		long effectFrame = frame.getId() + skillFunction.getEffectFrame();
		//只给释放者有关
		long addValue = getAddHp(unit, unit, skill, skillFunction);
		List<Integer> defList = unitList.stream().mapToInt(e -> e.getId()).boxed().collect(Collectors.toList());
		unit.addBear(frame, new DrawBear(unit.getId(), defList, drawType, addValue, effectFrame, skillFunction));
	}
	
	public long getAddHp(Unit atk,Unit def,Skill skill,SkillFunction skillFunction,Object...args) {
		return (long)skillFunction.getFunctionValue(atk,def,skill,args);
	}
}
