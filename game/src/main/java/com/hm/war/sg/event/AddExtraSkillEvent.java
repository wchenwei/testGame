package com.hm.war.sg.event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddExtraSkillEvent extends Event{
	private int skillId;
	private long frame;//持续帧数
	private int funcId;
	
	public AddExtraSkillEvent(int id, int skillId,long frame,int funcId) {
		super(id, EventType.AddExtraSkill);
		this.frame = frame;
		this.funcId = funcId;
		this.skillId = skillId;
	}

	@Override
	public String toString() {
		return "添加额外技能:"+skillId+"<br>";
	}
}
