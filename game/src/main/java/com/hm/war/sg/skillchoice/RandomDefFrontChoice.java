package com.hm.war.sg.skillchoice;

import com.hm.util.RandomUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class RandomDefFrontChoice extends BaseSkillChoice{
	private int num;//随机个数
	
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		return RandomUtils.randomEleList(ChoiceUtils.getFirstRowUnit(defGroup.getLifeUnit()), num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
}