package com.hm.war.sg.bear;

import com.google.common.collect.Lists;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.HurtLinkBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.HurtLinkBuffEvent;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class HurtLinkBuffBear extends BuffBear{
	private List<Integer> linkIds = Lists.newArrayList();

	public HurtLinkBuffBear(int atkId, long continueFrame, long effectFrame,
                            SkillFunction skillFunction, List<Integer> linkIds) {
		super(atkId, UnitBufferType.HurtLink, continueFrame, effectFrame, skillFunction);
		this.linkIds = linkIds;
	}
	
	@Override
	public Event createEvent(Unit unit) {
		return new HurtLinkBuffEvent(unit.getId(), this.buffType,this.continueFrame,skillFunction.getId(),linkIds);
	}
	
	public BaseBuffer createBuffer() {
		long endFrame = continueFrame;
		if(endFrame > 0) {
			endFrame += getEffectFrame();
		}
		return new HurtLinkBuff(endFrame, value, linkIds);
	}
}
