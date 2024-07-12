
package com.hm.enums;



/**
 * 秘宝类型
 * @author xjt
 * @date 2020年3月12日11:38:56 
 * @version 1.0
 */
public enum RareTreasureType {
	Box1(1,"小箱子（单个道具）"),
	Box2(2,"小箱子"),
	Box3(3,"大箱子"),
	Enemy1(4,"npc-简单（明）"),
	Enemy2(5,"npc-困难（明）"),
	Enemy3(6,"npc-极难（明）"),
	Enemy4(7,"npc-简单（暗）"),
	Begin(8,"出生点"),
	Door(9,"出口"),
	Key(10,"钥匙"),
	ReLive(11,"复活"),
	Repair(12,"维修"),
	Hunter(13,"猎人"),
	Obstacle(14,"路障"),
	;
	private RareTreasureType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;
	
	public static RareTreasureType getBuildType(int type) {
		for (RareTreasureType buildType : RareTreasureType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

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

	public static RareTreasureType getEvent(int type) {
		for(RareTreasureType t:RareTreasureType.values()){
			if(t.getType()==type){
				return t;
			}
		}
		return null;
	}
	
	public static boolean isCanUse(int type){
		return type!=Begin.getType()&&type!=Enemy1.getType()&&type!=Enemy2.getType()&&type!=Enemy3.getType()&&type!=Enemy4.getType();
	}
	
	public static boolean isEnemy(int type){
		return type==Enemy1.getType()||type==Enemy2.getType()||type==Enemy3.getType()||type==Enemy4.getType();
	}
	
}

