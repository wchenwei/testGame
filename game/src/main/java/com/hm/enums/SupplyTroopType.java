package com.hm.enums;

/**
 * @Description: 补给部队类型
 * @author siyunlong  
 * @date 2018年2月24日 上午10:26:11 
 * @version V1.0
 */
public enum SupplyTroopType {
	None(0,"空闲"),
	Run(1,"正在配送"),
	Over(2,"结束"),
	;
	
	private SupplyTroopType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}
	
	public static SupplyTroopType getType(int type) {
		for (SupplyTroopType buildType : SupplyTroopType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}
