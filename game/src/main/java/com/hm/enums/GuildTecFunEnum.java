package com.hm.enums;

/**
 * 
 * ClassName: GuildTecFunEnum. <br/>  
 * Function: 科技类型分类. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年1月10日 上午9:30:56 <br/>  
 *  
 * @author zxj  
 * @version
 */
public enum GuildTecFunEnum {
	UnitAttr(1,"属性增加X"),
	SkillLv(2,"获得技能等级X"),
	CityResource(3,"战术类使用次数X"),
	WarResource(4,"城市产出资源增加X"),
	RateCron(6,"额外掉落功勋"),
	CityResDoub(7,"每隔X小时大城双倍产出"),
	CityResHour(8,"每小时获得随机X城市产出"),
	CityResBox(9,"城市产出额外获得X级军需箱"),
	TroopAdvance(10,"突进所需部队倍率X"),
	ArmyReHp(11,"部队回血速度增加X"),
	Credit(12,"城市产出功勋增加n，固定值"),
	ResDoubChance(13,"2级城市双倍产出的概率"),
	TroopMoveSpeed(14,"世界部队移动速度增加"),
	;
	
	/**
	 * @param type
	 * @param desc
	 */
	private GuildTecFunEnum(int type, String desc) {
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
}
