package com.hm.war.sg.skillchoice;

import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class DeathChoice extends BaseSkillChoice{
	private boolean isSelf;//是否是己方
	private boolean containSelf;//是否包含自己
	
	public DeathChoice(boolean isSelf, boolean containSelf) {
		super();
		this.isSelf = isSelf;
		this.containSelf = containSelf;
	}

	@Override
	public List<Unit> choiceTargets(Unit atk, UnitGroup atkGroup, UnitGroup defGroup) {
		UnitGroup unitGroup = isSelf ? atkGroup:defGroup;
		return unitGroup.getUnits().stream()
				.filter(e -> e.isDeath())//所有死亡
				.filter(e -> containSelf || e.getId() != atk.getId())
				.collect(Collectors.toList());
	}
	
}
