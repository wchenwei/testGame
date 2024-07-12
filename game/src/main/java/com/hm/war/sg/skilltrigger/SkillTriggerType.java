package com.hm.war.sg.skilltrigger;

import com.hm.enums.TankAttrType;

/**
 * @Description: 技能触发
 * @author siyunlong  
 * @date 2018年9月27日 下午4:43:06 
 * @version V1.0
 */
public enum SkillTriggerType {
	None(0,"一定触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new NoneTrigger();
		}
	},
	Country(1,"自己国家类型触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new AtkCountryTrigger();
		}
	},//1:1
	Country2(2,"敌方国家类型触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new DefCountryTrigger();
		}
	},//1:1
	HpLowThan(3,"攻击者血量低于触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpLowThanTrigger(true);
		}
	},
	HpHighThan(4,"攻击者血量高于触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpHighThanTrigger(true);
		}
	},
	Rate(5,"概率") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new RateTrigger();
		}
	},
	AtkBuff(6,"攻击有某buff触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new BuffTrigger(true);
		}
	},
	DefBuff(7,"防御有某buff触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new BuffTrigger(false);
		}
	},
	DefHpLowThan(8,"技能目标血量低于触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpLowThanTrigger(false);
		}
	},
	DefHpHighThan(9,"技能目标攻击者血量高于触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpHighThanTrigger(false);
		}
	},
	NormalAtkBei(10,"第几次普攻触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new NormalAtkBeiTrigger();
		}
	},
	AtkTankType(11,"施放者是某类型战车触发  格式：11#类型") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new TankTypeTrigger(true);
		}
	},
	DefTankType(12,"目标是某类型战车触发  格式：12#类型") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new TankTypeTrigger(false);
		}
	},
	SelfMpLow(13,"自己能量等于低于n%触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new MpTrigger(true, 0);
		}
	},
	SelfMpHigh(14,"自己能量高于n%触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new MpTrigger(true, 1);
		}
	},
	DefMpLow(15,"技能目标能量等于低于n%触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new MpTrigger(false, 0);
		}
	},
	DefMpHigh(16,"技能目标能量高于n%触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new MpTrigger(false, 1);
		}
	},
	DefAtkMaxHp(17,"最大血量触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new MaxHpTrigger();
		}
	},
	
	HpCompare(18,"技能目标当前血量低于释放者的当前血量") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpCompareTrigger(true);
		}
	},
	AttrCompare(19,"技能目标防御低于释放者的防御") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new AttrCompareTrigger(TankAttrType.DEF,true);
		}
	},
	HpCompare2(20,"技能目标当前血量高于等于自己的当前血量") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new HpCompareTrigger(false);
		}
	},
	AttrCompareDef(21,"技能目标防御高等于于自己的防御力") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new AttrCompareTrigger(TankAttrType.DEF,false);
		}
	},
	AtkNoBuff(22,"施放者身上没有某种buff  22#buff类型") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new NoneBuffTrigger(true);
		}
	},
	DefNoBuff(23,"技能目标身上没有有某种buff  23#buff类型") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new NoneBuffTrigger(false);
		}
	},
	AtkCompare(24,"技能目标攻击低于自己的攻击力") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new AttrCompareTrigger(TankAttrType.ATK,true);
		}
	},
	AtkCompareDef(25,"技能目标攻击高于等于自己的攻击力") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new AttrCompareTrigger(TankAttrType.ATK,false);
		}
	},
	NormalAtkColDefCountTrigger(26,"当前目标所在列的敌军数量") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new NormalAtkColDefCountTrigger();
		}
	},
	FrameNoReduceHpTrigger(27,"x帧数内血量没有减少触发") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new FrameNoReduceHpTrigger();
		}
	},

	Trigger29(29,"场上队友数量大于等于触发29#队友人数") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new LifeUnitTrigger(this,true,true,false);
		}
	},
	Trigger30(30,"场上队友数量小于触发30#队友人数") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new LifeUnitTrigger(this,true,false,false);
		}
	},
	Trigger31(31, "技能目标所在排数量大于等于触发31#数量") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new UnitPosTrigger(this, false, true, true);
		}
	},
	Trigger32(32, "技能目标所在排数量小于触发32#数量") {
		@Override
		public BaseTriggerSkill buildTriggerCon() {
			return new UnitPosTrigger(this, false, true, false);
		}
	},
	;
	
	private SkillTriggerType(int type,String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public static SkillTriggerType getType(int type) {
		for (SkillTriggerType buildType : SkillTriggerType.values()) {
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
	
	public abstract BaseTriggerSkill buildTriggerCon();
}
