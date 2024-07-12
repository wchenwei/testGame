package com.hm.enums;

/**
 * 
 * @Description: 现代战争事件类型
 * @author xjt  
 * @date 2019年9月4日14:36:46
 * @version V1.0
 */
public enum ModernBattleEventType {
	Recovery(1,"回血"),
	Skip(2,"跳过小关卡"),
	ChangeTroop(3,"更换部队"),
	Resurrection(4,"复活"),
	Skill(5,"技能"),
	NormalNpc(6,"普通npc"),
	EliteNpc(7,"宝藏npc"),
	Boss(8,"boss"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private ModernBattleEventType(int type, String desc) {
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
		return type==NormalNpc.getType()||type==EliteNpc.getType()||type==Boss.getType();
	}
	
	public static ModernBattleEventType getModernBattleEventType(int type){
		for(ModernBattleEventType eventType:ModernBattleEventType.values()){
			if(eventType.getType()==type){
				return eventType;
			}
		}
		return null;
	}
}
