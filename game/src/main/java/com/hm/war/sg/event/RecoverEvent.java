package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;

public class RecoverEvent extends Event{
	private long hp;//当前hp
	private long addHp;//回血量
	private double mp;//当前蓝量
	private double addMp;
	private int funcId;
	
	public RecoverEvent(Unit unit,long addHp,double addMp,int funcId) {
		super(unit.getId(), EventType.RecoverHp);
		this.addHp = addHp;
		this.addMp = addMp;
		this.hp = Math.max(unit.getHpEngine().getHp(), 0);
		this.mp = unit.getMpEngine().getMp();
		this.funcId = funcId;
	}

    public RecoverEvent(Unit unit) {
        this(unit, 0, 0, 0);
    }
	
	public RecoverEvent() {
		super();
	}


	@Override
	public void showHp() {
		System.err.println("回复："+getId()+"="+this.hp+"  "+this.addHp);
	}
	@Override
	public String toString() {
		return this.funcId+ "回血: "+id+" -> addHp:"+ addHp +" addMp:"+this.addMp;
	}
}
