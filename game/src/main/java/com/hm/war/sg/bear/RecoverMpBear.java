package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.RecoverEvent;
import com.hm.war.sg.unit.Unit;

public class RecoverMpBear extends Bear{
	private double addMp;
	private int funcId;
	
	public RecoverMpBear(int atkId, double addMp,long effectFrame,int funcId) {
		super(atkId, effectFrame);
		this.addMp = addMp;
		this.funcId = funcId;
	}
	
	public int getFuncId() {
		return funcId;
	}



	@Override
	public Event createEvent(Unit unit) {
		return new RecoverEvent(unit, 0,addMp,funcId);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		boolean isSuc = unit.getMpEngine().addMpForSkill(frame,addMp);
		if(isSuc) {
			//重新计算增加的值
			this.addMp = unit.getMpEngine().calTrueAddMp(this.addMp);
		}
		return isSuc;
	}
	
}
