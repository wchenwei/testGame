package com.hm.enums;

public enum QueueState {
	Wait(0,"等待"),
	Run(1,"正在进行"),
	Complete(2,"完成"),
	;
	
	private QueueState(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	/**
	 * 是否处于升级状态
	 * @param state
	 * @return
	 */
	public static boolean isRunning(QueueState state){
		return state==Run;
	}
}
