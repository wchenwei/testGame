package com.hm.war.sg.skillchoice;

import com.hm.util.RandomUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class RandomSelfNomeChoice extends BaseSkillChoice{
	private int num;//随机个数
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		List<Unit> lifeUnits = atkGroup.getLifeUnit();
		lifeUnits.remove(atk);
		return RandomUtils.randomEleList(lifeUnits, num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
}
