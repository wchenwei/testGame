package com.hm.war.sg.effect;

/**
 * @Description: 技能效果
 * @author siyunlong  
 * @date 2018年9月27日 下午4:43:06 
 * @version V1.0
 */
public enum SkillEffectType {
	Hurt(1,"伤害"),
	Stun(2,"眩晕"),
	Silent(3,"沉默"),
	AttrBuff(4,"添加buff"),
	RecoverHp(5,"回血"),
	ReduceHp(6,"减血"),
	ReduceHurt(7,"普攻减伤"),
	AddHurt(8,"普攻增伤"),
	ClearBuff(9,"清除buff"),
	ManyHurt(10,"多次伤害"),
	Shield(11,"护盾"),
	MpChange(12,"蓝量变化"),
	Mark(13,"标记"),
	HurtChange(14,"收到伤害增加/减少"),
	Crit(15,"暴击"),
	DoubleHit(16,"连击"),
	DrawEffect(17,"吸取"),
	OfferHpEffect(18,"牺牲血量"),
	NormalAtkAddHpEffect(19,"普攻吸血"),
	ReviveEffect(20,"复活"),
	BackHurtEffect(21,"反伤"),
	HurtLink(22,"致命连接"),
	AttrIntervalChangeBuff(23,"间隔属性变化"),
	AddExtraSkill(24,"添加额外技能"),
	StealAttr(25,"偷取属性"),
	BeheadedEffect(26,"斩杀"),
	ResetNormalCDEffect(27,"重置普工"),
	AddAircraftOilEffect(28, "增加飞机机油"),
	StealBuffEffect(29, "偷buff"),
	HurtShareEffect(30, "伤害分享"),

	;
	
	private SkillEffectType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public static SkillEffectType getSkillType(int type) {
		for (SkillEffectType buildType : SkillEffectType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
}
