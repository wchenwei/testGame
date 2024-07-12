package com.hm.war.sg.buff;

import lombok.Getter;

/**
 * 
 * @Description: 坦克buff类型
 * @author siyunlong  
 * @date 2018年9月27日 下午5:55:42 
 * @version V1.0
 */
@Getter
public enum UnitBufferType {
	AttrBuff(1,"属性buff"),
	StunBuff(2,"眩晕"),
	SilentBuff(3,"沉默"),
	ShieldBuff(4,"护盾buff"),
	SkillPreBuff(5,"技能前摇"),
	MarkBuff(6,"标记buff"),
	ContinueBuff(7,"持续伤害/回复"),
	HurtEffectBuff(8,"伤害影响buff -收到伤害增加/减少"),
	NotStunBuff(9,"不能眩晕buff"),
	CureEffectBuff(10,"治愈效果增加/减少 "),
	DrawBuff(11,"吸取-一条线的技能"),
	NoDeath(12,"不死buff "),
	InvincibleBuff(13,"无敌buff"),
	AtkSuckBlood(14,"吸血"),
	BackHurtBuff(15,"反伤"),
	AddAtkTargets(16,"增加攻击目标"),
	HurtLink(17,"致命连接buf"),
	NotReviveBuff(18,"不能复活buf"),
	RateNotStunBuff(19,"概率不眩晕buf"),
	RateDodgeBuff(20,"概率闪避buf"),
	TauntBuff(21,"嘲讽buf"),
	NoAtkBuff(22,"不能攻击buf"),
	NextDeathBuff(23,"下局死亡buf"),
	SmallSkillSilentBuff(24,"无法释放小技能buf"),
	RandomAddAtkTargets(25,"概率增加攻击目标"),
	DodgeBuff(26,"闪避buff"),
	RateNoSilentBuff(27,"概率免疫沉默buff"),
	NotAddMpBuff(28,"能量无法增加buff"),
	NotAddMpSkillBuff(29,"技能能量无法增加buff"),
	HurtHp10EffectBuff(30,"伤害大于血量10%影响buff -收到伤害增加/减少"),
	ShieldBuffBackHp(31,"护盾破裂后扣血buff"),
	HurtSelfLink(32,"致命连接buf-队友伤害计算我的血量"),
	ReduceMpBuff(33,"增加mp时,减少值buff"),
	ReduceStunBuff(34,"减少眩晕时间buff"),
	AtkDodgeBuff(35,"普攻missbuff"),
	HurtLink2(36,"致命连接buf-目标死之后重新连接"),
	FixedBodyBuff(37,"定身buff"),
	ShieldBuffAddMp(38,"护盾破裂后增加mp"),
	NoShieldBuff(39,"不触发护盾buff"),
	HurtHp80EffectBuff(40,"伤害大于血量80%影响buff -收到伤害增加/减少"),
	TransHurtBuff(41, "转移伤害为持续buff伤害"),
	ShedBloodBuff(42, "流血buff,用于处理伤害转化成持续掉血buff"),
	DefCritResReduce(43, "对面爆伤抗性减少"),
	RateNoSkillHurt(44, "概率免疫技能伤害buff"),
	RateNoReduceMP(45, "概率免疫减少mp buff"),
	RateNoFixedBody(46, "概率免疫定身buff"),

	;
	
	private UnitBufferType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	private String desc;
	public int getType() {
		return type;
	}
	
	public static boolean isShieldBuff(UnitBufferType buffType) {
		return buffType == UnitBufferType.ShieldBuff || buffType == UnitBufferType.ShieldBuffBackHp
				|| buffType == UnitBufferType.ShieldBuffAddMp;
	}
	
	public static UnitBufferType getType(int type) {
		for (UnitBufferType buildType : UnitBufferType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}
