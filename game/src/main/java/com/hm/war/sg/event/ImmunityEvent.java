package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ImmunityEvent extends Event{

	public ImmunityEvent(Unit unit) {
		super(unit.getId(), EventType.SkillImmunity);

	}
	
	@Override
	public String toString() {
        return getId()+"技能免疫";
	}
	@Override
	public void showHp() {
		System.err.println(getId()+"技能免疫");
	}
}
