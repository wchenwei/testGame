package com.hm.war.sg.skillchoice;

import com.hm.util.RandomUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class TankTypeChoice extends BaseSkillChoice{
	private boolean isAtk;
	private int type;
	private int num;
	
	public TankTypeChoice(boolean isAtk) {
		this.isAtk = isAtk;
	}

	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		List<Unit> lifeUnit = isAtk?atkGroup.getLifeUnit():defGroup.getLifeUnit();
		return RandomUtils.randomEleList(ChoiceUtils.getUnitByTank(lifeUnit,type), num);
	}
	
	@Override
	public void loadParm(String parm) {
		this.type = Integer.parseInt(parm.split(":")[0]);
		this.num = Integer.parseInt(parm.split(":")[1]);
	}
}
