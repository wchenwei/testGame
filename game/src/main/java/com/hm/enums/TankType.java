
package com.hm.enums;

/**
 * 
 * date: 2019年7月2日16:38:25
 *  
 * @author xjt  
 * @version
 */
public enum TankType {
	DefTank(1,"防御型坦克"),
	AtackTank(2,"进攻型坦克"),
	AidTank(3,"支援型坦克"),
	AssistTank(4,"辅助型坦克"),
	;
	
	
	
	private TankType(int type,String desc){
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
	
	public static TankType getTankType(int index){
		for (TankType type : TankType.values()) {
			if(index == type.getType()) return type; 
		}
		return null;
	}
	
}

