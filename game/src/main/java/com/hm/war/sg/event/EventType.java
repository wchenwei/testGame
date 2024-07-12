package com.hm.war.sg.event;

import java.util.Arrays;

public enum EventType {
	NormalAtk(1,"普通攻击"),
	SkillPre(2,"技能前摇"),
	SkillRelease(3,"技能释放"),
	Hurt(4,"收到伤害"),
	RecoverHp(5,"单次回血"),
	StateSync(6,"状态同步"), //废弃
	ContineEvent(7,"持续回复/伤害"),//废弃
	BuffEvent(8,"buff事件"),
	ReviveEvent(9,"复活"),
	CleanEvent(10,"清除event"),
//	SuckBlood(11,"攻擊吸血event"),//废弃
	DrawEevet(12,"吸取event"),
	DeathEevet(13,"死亡event"),
	AddExtraSkill(14,"增加额外技能"),
	DropItemEvent(15,"掉落"),
	AircraftEvent(16,"航空母舰event"),
	SkillImmunity(17,"技能免疫"),
	ImmunityEvent(18,"免疫事件"),

	StunShow(19,"眩晕飘字"),
	NoStunShow(20,"免疫眩晕飘字"),
	NoSilentShow(21,"免疫沉默飘字"),
	NoFixBodyShow(22,"免疫定身飘字"),
	FullHp(23,"满血"),


	ShowAttr(1001, "测试"),
	ShowEvent(1002, "测试"),
	;
	
	private EventType(int type, String desc) {
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

	public static EventType getType(int type){
		return Arrays.stream(EventType.values()).filter(t->t.getType()==type).findFirst().orElse(null);
	}
}
