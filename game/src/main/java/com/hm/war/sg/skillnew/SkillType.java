package com.hm.war.sg.skillnew;

/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author siyunlong  
 * @date 2018年9月7日 下午1:57:28 
 * @version V1.0
 */
public enum SkillType {
	ActiveSkill(1,"主动技能") ,
	PassiveAtkSkill1(2,"攻击-额外技能") ,//在攻击时 有条件 触发某个额外技能 
	PassiveDefSkill1(3,"防御被动技能") ,//受伤时 有条件 触发某个额外技能 
	ComOutSkill(4,"出场技能") ,
	TriggerSkill(5,"小技能") ,
	AttrSkill(6,"属性技能") , //根据玩家状态触发属性技能
	SelfDeathSkill(7,"我方死亡触发技能") ,
	DefDeathSkill(8,"敌方死亡触发技能") ,
	AtkBeDodge(9,"自己普攻被别人闪避，额外技能") ,
	AtkDodge(10,"自己闪避普攻，额外技能") ,
	ReleaseBigSkill(11,"自己释放大招时，额外技能") ,
	Revive(12,"复活") ,
	AtkCritSkill(13,"普攻攻击暴击触发技能") ,
	DeadlyHurt(14,"受到致命伤害触发技能,对自己释放技能") ,
	KillTankDoSkill(15," 自己杀死敌人后，触发技能") ,
	DefCritSkill(16,"普攻攻击被暴击触发技能") ,
	MyDeathSkill(17,"自己死亡触发技能") ,
	FrameSkill(18,"每帧技能触发") ,
	NormalAtk(19,"每次普攻触发技能（不受沉默影响）") ,
	ReleaseBigSkillForDef(20,"敌方释放大招时触发") ,
	DeathLifeSelf(21,"自己死亡被复活触发") ,
	AircraftSkill(23, "释放飞机技能触发"),
	BeNormalAtkHurt(25, "收到普工伤害时触发"),

	MLNormalSkill(101, "指挥官普攻技能"),
	MLBigSkill(102, "指挥官大招技能"),

	;
	
	private SkillType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public static SkillType getSkillType(int type) {
		for (SkillType buildType : SkillType.values()) {
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
