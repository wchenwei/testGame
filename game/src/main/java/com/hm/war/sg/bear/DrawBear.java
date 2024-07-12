package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.BuffKind;
import com.hm.war.sg.buff.DrawBuff;
import com.hm.war.sg.event.DrawEvent;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class DrawBear extends Bear{
	private long addValue;
	private int type;
	private List<Integer> defList;

	private int funcId;
	private int interval;
	private long count;//次数
	private BuffKind buffKind;
	private SkillFunction skillFunction;
	
	public DrawBear(int atkId, List<Integer> defList,int type,long addValue,long effectFrame,SkillFunction skillFunction) {
		super(atkId, effectFrame);
		this.addValue = addValue;
		this.type = type;
		this.defList = defList;
		this.funcId = skillFunction.getId();
		this.interval = skillFunction.getIntervalFrame();
		this.count = (int)(skillFunction.getContinuedFrame()/interval);
		this.buffKind = skillFunction.getBuffKind();
		this.skillFunction = skillFunction;
	}

	@Override
	public Event createEvent(Unit unit) {
		return new DrawEvent(unit.getId(), defList, type, interval*count, funcId);
	}

	@Override
	public boolean doUnit(Frame frame, Unit unit) {
		DrawBuff drawBuff = new DrawBuff(defList, addValue, type, interval, count, getEffectFrame(), buffKind,funcId);
		drawBuff.setDelBuffType(this.skillFunction.getDelBuffType());
		drawBuff.setBuffUnitId(getAtkId());
		unit.addBuffer(drawBuff);
		return true;
	}
	
}
