package com.hm.war.sg.skillchoice;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class DefCurAtkColsChoice extends BaseSkillChoice{
	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		List<Unit> curDefs = atk.getAtkEngine().choiceTarget(defGroup);
		if(CollUtil.isEmpty(curDefs)) {
			return Lists.newArrayList();
		}
		Unit curUnit = curDefs.get(0);
		return ChoiceUtils.listUnitsByIndexs(ChoiceUtils.getColsByIndex(curUnit.getIndex()), defGroup.getLifeUnit());
	}
	
}
