package com.hm.war.sg.skillchoice;

import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

//敌方攻击最高的单位
public class MaxtAtkChoice extends BaseSkillChoice{
	private boolean isAtk;
	
	public MaxtAtkChoice(boolean isAtk) {
		super();
		this.isAtk = isAtk;
	}

	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		if(isAtk) {
			return Lists.newArrayList(ChoiceUtils.getMaxAtkUnit(atkGroup.getLifeUnit()));
		}else{
			return Lists.newArrayList(ChoiceUtils.getMaxAtkUnit(defGroup.getLifeUnit()));
		}
	}
}
