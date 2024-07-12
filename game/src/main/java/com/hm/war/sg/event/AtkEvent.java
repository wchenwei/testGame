package com.hm.war.sg.event;

import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
public abstract class AtkEvent extends Event{
	protected List<Integer> targets = Lists.newArrayList();//目标点
	protected long flyFrame;//技能或者普攻的飞行帧
	
	public AtkEvent(int id, EventType type ,long flyFrame) {
		super(id, type);
		this.flyFrame = flyFrame;
	}
	
	public void addTarget(int target) {
		this.targets.add(target);
	}

}
