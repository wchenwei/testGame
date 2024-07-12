package com.hm.war.sg;

/**
 * 
 * @Description: 战斗队伍buff类型
 * @author siyunlong  
 * @date 2018年9月27日 下午5:55:42 
 * @version V1.0
 */
public enum TroopBufferType {
	MainCityProtect(1,"主城保护buff"),
	CitySurround(2,"城市被包围debuff"),
	;
	
	private TroopBufferType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	private String desc;
	public int getType() {
		return type;
	}
	
	public static TroopBufferType getType(int type) {
		for (TroopBufferType buildType : TroopBufferType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}
