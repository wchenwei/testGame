package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SkillPreEvent extends AtkEvent{
	private int skillId;//释放的技能id
	private double mp;//释放技能后mp
	
	public SkillPreEvent(Unit unit,int skillId,long flyFrame) {
		super(unit.getId(),EventType.SkillPre,flyFrame);
		this.skillId = skillId;
		if(unit.getMpEngine() != null) {
			this.mp = unit.getMpEngine().getMp();
		}
	}
	@Override
	public String toString() {
		return "释放技能前摇:"+skillId+"<br>";
	}
	
}
