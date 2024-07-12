package com.hm.war.sg.event;

import com.hm.libcore.util.gson.GSONUtils;
import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class NormalAtkEvent extends AtkEvent{
	private double atkCDRate;
	
	public NormalAtkEvent(Unit unit, int target,long flyFrame) {
		super(unit.getId(),EventType.NormalAtk,flyFrame);
		addTarget(target);
		this.atkCDRate = unit.getAtkEngine().getAtkCDRate();
	}

	public NormalAtkEvent(Unit unit, List<Integer> targetList, long flyFrame) {
		super(unit.getId(),EventType.NormalAtk,flyFrame);
		setTargets(targetList);
		this.atkCDRate = unit.getAtkEngine().getAtkCDRate();
	}

	@Override
	public String toString() {
		return "普通攻击: "+id+" -> "+GSONUtils.ToJSONString(targets);
	}
	
}

