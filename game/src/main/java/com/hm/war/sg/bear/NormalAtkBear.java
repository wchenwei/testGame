package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.NormalAtkEvent;
import com.hm.war.sg.unit.Unit;

/**
 * 
 * @Description: 普通攻击
 * @author siyunlong  
 * @date 2018年11月21日 下午2:00:57 
 * @version V1.0
 */
public class NormalAtkBear extends Bear{
	private HurtBear newBear;
	private int defId;
	private long flyFrame;
	private Unit atk;
	
	public NormalAtkBear(Unit atk, HurtBear newBear, int defId, long effectFrame, long flyFrame) {
		super(atk.getId(), effectFrame);
		this.newBear = newBear;
		this.defId = defId;
		this.flyFrame = flyFrame;
		this.atk = atk;
	}

	@Override
	public Event createEvent(Unit unit) {
		return new NormalAtkEvent(atk, defId,flyFrame);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		return true;
	}
	
}
