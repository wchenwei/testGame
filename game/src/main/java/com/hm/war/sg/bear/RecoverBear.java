package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.RecoverEvent;
import com.hm.war.sg.unit.Unit;

public class RecoverBear extends Bear{
	private long addHp;
	private int funcId;
	
	public RecoverBear(int atkId, long addHp,long effectFrame,int funcId) {
		super(atkId, effectFrame);
		this.addHp = addHp;
		this.funcId = funcId;
	}

	@Override
	public Event createEvent(Unit unit) {
		return new RecoverEvent(unit, addHp,0,funcId);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		unit.getHpEngine().addHp(this.addHp);
		return true;
	}

	public long getAddHp() {
		return addHp;
	}
	
}
