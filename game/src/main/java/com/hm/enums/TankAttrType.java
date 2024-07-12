package com.hm.enums;


/**
 * 
 * @Description: 国家
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum TankAttrType {
	ATK(1,"攻击"),
	DEF(2,"防御"),
	HP(3,"耐久"),
	HIT(4,"命中"),
	DODGE(5,"躲闪"),
	CRIT(6,"暴击"),
	CritDef(7,"防暴"),
	AtkPer(8,"攻击%"),
	DefPer(9,"防御%"),
	HpPer(10,"耐久%"),
	HitPer(11,"命中%"),
	DodgePer(12,"闪避%"),
	CritPer(13,"暴击%"),
	CritDefPer(14,"防暴%"),
	CritDamPer(15,"暴伤%"),
	CritResPer(16,"暴伤抵抗%"),
	AddAtkPer(17,"增伤%"),
	ReduceAtkPer(18,"减伤%"),
	BackAtkPer(19,"反伤%"),
	AddSkillPer(20,"技能增强%"),
	AddCurePer(21,"收到治疗增强%"),
	AtkCd(22,"攻击CD"),
	FirstAtkCd(23,"第一次出手CD"),
	MpRecover(24,"能量恢复加成%"),
	TroopRecoverHp(25,"单车对于部队回复速度"),
	AircraftReduce(26, "飞机减伤属性"),
	SkillHurtReduce(27, "收到技能伤害减免%"),
	FinalAddAtkPer(28,"最终增伤%"),
	FinalReduceAtkPer(29,"最终减伤%"),

	;
	private TankAttrType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	public static TankAttrType getType(int type) {
		for (TankAttrType buildType : TankAttrType.values()) {
			if(buildType.getType() == type) {
				return buildType;
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
