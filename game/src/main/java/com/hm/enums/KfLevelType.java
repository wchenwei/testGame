package com.hm.enums;

/**
 * 
 * @Description: 段位类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum KfLevelType {
	Bronze(1,"青铜",10,0),
	Silver(2,"白银",10,10),
	Gold(3,"黄金",10,10),
	SuperGold(4,"铂金",10,10),
	Diamonds(5,"钻石",3,10),
	;
	
	private KfLevelType(int type, String desc,int upNum,int downNum) {
		this.type = type;
		this.desc = desc;
		this.upNum = upNum;
		this.downNum = downNum;
	}

	private int type;
	private String desc;
	private int upNum;
	private int downNum;
	
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
	public static KfLevelType getKfLevelType(int type) {
		for (KfLevelType temp : KfLevelType.values()) {
			if(temp.getType() == type) {
				return temp;
			}
		}
		return null;
	}
	
	public KfLevelType getUpType() {
		if(this.getType() == Diamonds.getType()) {
			return null;
		}
		return getKfLevelType(this.getType()+1);
	}
	
	public KfLevelType getDownType() {
		if(this.getType() == Bronze.getType()) {
			return null;
		}
		return getKfLevelType(this.getType()-1);
	}

	public static KfLevelType[] AllLevelTypes = {Bronze,Silver,Gold,SuperGold,Diamonds};
}
