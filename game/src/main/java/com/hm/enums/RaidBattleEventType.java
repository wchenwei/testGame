package com.hm.enums;

/**
 * 
 * @Description: 现代战争事件类型
 * @author xjt  
 * @date 2019年9月4日14:36:46
 * @version V1.0
 */
public enum RaidBattleEventType {
	Item(1,"道具"),
	Package(2,"礼包"),
	Trap(6,"陷阱"),
	Hp(7,"加血"),
	Boss(8,"boss"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private RaidBattleEventType(int type, String desc) {
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
	
	public static boolean isFight(int type){
		return type==Boss.getType();
	}
	
	public static RaidBattleEventType getModernBattleEventType(int type){
		for(RaidBattleEventType eventType:RaidBattleEventType.values()){
			if(eventType.getType()==type){
				return eventType;
			}
		}
		return null;
	}
}
