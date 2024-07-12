package com.hm.enums;

/**
 * ClassName: PlayerTitleType. <br/>  
 * Function: 用户称号. <br/>  
 * date: 2019年4月28日 下午2:09:46 <br/>  
 * @author zxj  
 * @version
 */
public enum PlayerTitleType {
	PRESIDENT(1,"大总统"),//首都战获胜部落
	COMMANDER(2,"大元帅"),//首都战杀敌第一
	KING(3,"最强王者"),
	JzTitle(4,"局座称号"),
	AnniversaryTitle(5,"周年称号"),
	My5Year(6,"玛雅5周年"),
	WarGod(7,"战神"),
    AnniversaryTitle2(8, "2周年称号"),
	KFWorldWar(9, "跨服世界大战称号"),
	Anniversary (10, "首发两周年活动中签到7天获得"),

	LandMarshal(11, "陆地元帅【3天】"),
	WeiGeneral (12, "卫国将军【3天】"),

	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private PlayerTitleType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static PlayerTitleType getType(int type) {
		for (PlayerTitleType titleType : PlayerTitleType.values()) {
			if(titleType.getType() == type) {
				return titleType;
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
