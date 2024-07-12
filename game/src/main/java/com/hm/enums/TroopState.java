package com.hm.enums;

public enum TroopState {
	None(0,"空闲：满血时，未参与任何PVP战斗"),
//	Recovery(1,"回血：正在恢复血量的过程"),
	Death(2,"死亡：整支部队全部阵亡，为死亡状态，回满血后状态变更"),
	Move(3,"移动：部队从一点向另一点转移"),
	Fight(4,"战斗(上阵)：在PVP城战中，攻守队列的前三名为战斗(上阵)状态"),
	PvpOneByOne(5,"单挑：在PVP城战中，主动或被动单挑敌对部队"),
	FightWait(6,"等待：在PVP城战中，还未上阵"),
	;
	
	private TroopState(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static boolean isCanMove(int state) {
		return state == None.getType() || state == Move.getType() || state == FightWait.getType();
	}
	public static boolean isFightState(int state) {
		return state == Fight.getType() || state == PvpOneByOne.getType() || state == FightWait.getType();
	}
	
	public static boolean isIdleState(int state) {
		return state == TroopState.None.getType() 
//				|| state == TroopState.Recovery.getType()
				;
	}
}
