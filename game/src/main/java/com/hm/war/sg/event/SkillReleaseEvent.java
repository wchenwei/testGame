package com.hm.war.sg.event;

import com.hm.war.sg.unit.AtkEngine;
import com.hm.war.sg.unit.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SkillReleaseEvent extends AtkEvent{
	private int skillId;//释放的技能id
	private double atkCDRate = 1;
	
	public SkillReleaseEvent(Unit unit, int skillId, List<Integer> targets, long flyFrame) {
		super(unit.getId(),EventType.SkillRelease,flyFrame);
		this.skillId = skillId;
		this.targets = targets;

		AtkEngine atkEngine = unit.getAtkEngine();
		if(atkEngine != null) {
			this.atkCDRate = atkEngine.getAtkCDRate();
		}
	}

	@Override
	public String toString() {
		return "释放技能:"+skillId+"<br>";
	}
}
