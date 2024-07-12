package com.hm.war.sg.event;

import com.google.common.collect.Lists;
import com.hm.war.sg.buff.UnitBufferType;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class HurtLinkBuffEvent extends BuffEvent{
	private List<Integer> linkIds = Lists.newArrayList();
	
	public HurtLinkBuffEvent(int id, UnitBufferType buffType, long frame, int funcId,List<Integer> linkIds) {
		super(id, buffType, frame, funcId);
		this.linkIds = Lists.newArrayList(linkIds);
	}
	
}
