package com.hm.enums;


/**
 * 
 * @Description: 资质类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum TankStateType {
	None(0,"空闲"),
	War(1,"战斗"),
	;
	
	private TankStateType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static TankStateType getType(int type) {
		for (TankStateType stateType : TankStateType.values()) {
			if(stateType.getType() == type) {
				return stateType;
			}
		}
		return null;
	}

	
	private int type;
	private String desc;

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
}
