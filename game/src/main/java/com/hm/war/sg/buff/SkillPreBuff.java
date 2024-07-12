package com.hm.war.sg.buff;

public class SkillPreBuff extends BaseBuffer{

	public SkillPreBuff(long endFrame,int skillId) {
		super(UnitBufferType.SkillPreBuff,endFrame,BuffKind.None);
		this.skillId = skillId;
	}
}
