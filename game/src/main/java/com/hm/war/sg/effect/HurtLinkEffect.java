package com.hm.war.sg.effect;

import cn.hutool.core.collection.CollUtil;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtLinkBuffBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 致命连接
 * @author siyunlong  
 * @date 2018年11月9日 下午4:48:33 
 * @version V1.0
 */
public class HurtLinkEffect extends BaseSkillEffect{
	
	public HurtLinkEffect() {
		super(SkillEffectType.HurtLink);
	}
	
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		if(CollUtil.isEmpty(unitList)) {
			return;
		}
		long effectFrame = frame.getId()+skillFunction.getEffectFrame();
		List<Integer> linkIds = unitList.stream().map(e -> e.getId()).collect(Collectors.toList());
		
		HurtLinkBuffBear bear = new HurtLinkBuffBear(unit.getId(),
				skillFunction.getContinuedFrame(), effectFrame, skillFunction, linkIds);
		bear.setValue(getValue(unit, unitList.get(0), skill, skillFunction));
		unit.addBear(frame, bear);
	}
	
	public double getValue(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return skillFunction.getFunctionValue(atk,def,skill);
	}
}