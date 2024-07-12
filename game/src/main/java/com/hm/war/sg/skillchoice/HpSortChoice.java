package com.hm.war.sg.skillchoice;

import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class HpSortChoice extends BaseSkillChoice{
	private boolean isAtk;//是否是攻击方
	private boolean isTop;//按照最大排序
	private boolean isBfb;//是否按照比例
	private boolean isSelf;//是否包含自己
	private int num;
	

	public HpSortChoice(boolean isAtk, boolean isTop, boolean isBfb) {
		this.isAtk = isAtk;
		this.isTop = isTop;
		this.isBfb = isBfb;
		this.isSelf = true;
	}
	public HpSortChoice(boolean isAtk, boolean isTop, boolean isBfb, boolean isSelf) {
		this.isAtk = isAtk;
		this.isTop = isTop;
		this.isBfb = isBfb;
		this.isSelf = isSelf;
	}

	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		UnitGroup tempGroup = isAtk ? atkGroup:defGroup;
		List<Unit> findList = tempGroup.getLifeUnit();
		if(isAtk && !isSelf) {
			findList.remove(atk);
		}
		List<Unit> unitList = Lists.newArrayList();
		if(isBfb) {
			unitList = ChoiceUtils.getHpRateUnit(findList,isTop);
		}else{
			unitList = ChoiceUtils.getHpUnit(findList,isTop);
		}
		return unitList.subList(0, Math.min(unitList.size(), this.num));
	}
	
	@Override
	public void loadParm(String parm) {
		this.num = Integer.parseInt(parm);
	}
}
