package com.hm.war.sg.skillchoice;

import com.hm.enums.TankAttrType;

/**
 * @Description: 技能选择类型
 * @author siyunlong  
 * @date 2018年10月11日 下午3:43:29 
 * @version V1.0
 */
public enum SkillChoiceType {
	None(0,"None") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SelfChoice();
		}
	},
	Self(1,"自身") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SelfChoice();
		}
	},
	NormalAtk(2,"跟普攻目标相同") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new NormalAtkSameChoice();
		}
	},
	HpRateLowDefMin(3,"敌方剩余血量百分比最低") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new HpSortChoice(false,false,true);
		}
	},
	HpRateLowSelfMin(4,"己方剩余血量百分比最低") {
		@Override
		public BaseSkillChoice buildChoice() {
			return  new HpSortChoice(true,false,true);
		}
	},
	HpRateHighDefMax(5,"敌方剩余血量百分比最高") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new HpSortChoice(false,true,true);
		}
	},
	HpRateHighSelfMax(6,"己方剩余血量百分比最高") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new HpSortChoice(true,true,true);
		}
	},
	HpLowDefMin(7,"敌方血量最低") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new HpSortChoice(false,false,false);
		}
	},
	HpLowSelfMin(8,"己方血量最低") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new HpSortChoice(true,false,false);
		}
	},
	DefFrontRow(9,"敌方最前排所有单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new FrontDefRowChoice();
		}
	},
	SelfFrontRow(10,"己方最前排所有单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new FrontSelfRowChoice();
		}
	},
	DefBackRow(11,"敌方最后排所有单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new LastDefRowChoice();
		}
	},
	SelfBackRow(12,"己方最后排所有单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new LastSelfRowChoice();
		}
	},
	DefAll(13,"敌方全体") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new AllDefChoice();
		}
	},
	SelfAll(14,"己方所有单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new AllSelfChoice();
		}
	},
	DefRandom(15,"敌方随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomDefChoice();
		}
	},
	SelfRandom(16,"己方随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomSelfChoice();
		}
	},
	DefFrontRandom(17,"敌方最前排随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomDefFrontChoice();
		}
	},
	SelfFrontRandom(18,"己方最前排随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomSelfFrontChoice();
		}
	},
	CountryDef(19,"敌方-国家阵营战车") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new CountryDefChoice();
		}
	},
	CountrySelf(20,"己方-国家阵营战车") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new CountrySelfChoice();
		}
	},
	DefCurAtkCols(21,"当前敌方目标所在的一列全部单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new DefCurAtkColsChoice();
		}
	},
	
	DefLastRandom(22,"敌方最后排随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomDefFrontChoice();
		}
	},
	SelfLastRandom(23,"己方最后排随机单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomSelfLastChoice();
		}
	},
	DefAtkMax(24,"敌方攻击最高的单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new MaxtAtkChoice(false);
		}
	},
	DefForAtk(25,"防御反弹") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new DefForAtkChoice();
		}
	},
	DefRandomRepeat(26,"敌方随机可重复") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomRepeatDefChoice();
		}
	},
	DeathFriendNotSelf(27,"其它死亡队友（不包含自身）") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new DeathChoice(true, false);
		}
	},
	DefTypeChoice(28,"敌军x类型战车随机n单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new TankTypeChoice(false);
		}
	},
	AtkTypeChoice(29,"友军x类型战车随机n单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new TankTypeChoice(true);
		}
	},
	
	DefLineChoice(30,"自己所在一排的友军") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new DefLineChoice();
		}
	},
	DefColumnChoice(31,"自己所在一列的友军") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new DefColumnChoice();
		}
	},
	AtkAtkMax(32,"攻击方攻击最高的单位") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new MaxtAtkChoice(true);
		}
	},
	RandomSelfNomeChoice(33,"除了自己之外的随机n队友") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new RandomSelfNomeChoice();
		}
	},
	NoTargetRandomChoice(34,"除了当前目标之外的随机n敌军") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new NoTargetRandomChoice();
		}
	},
	MpRateHighDefChoice(35,"敌方当前能量与能量上限百分比最低#人数") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new MpSortChoice(false,false,true);
		}
	},
	MpHighDefChoice(36,"敌方当前能量与能量上限百分比最高#人数") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new MpSortChoice(false,true,true);
		}
	},
	HpRateLowSelfMin2(37,"（除了自己之外）己方剩余血量百分比最低#人数") {
		@Override
		public BaseSkillChoice buildChoice() {
			return  new HpSortChoice(true,false,true,false);
		}
	},
    NotPreRow(38, "除了前排的其它随机几名友军单位#人数") {
        @Override
        public BaseSkillChoice buildChoice() {
            return new SkillChoice38(true);
        }
    },
    SkillChoice39(39, "敌方攻击最高的n个单位39#2") {
        @Override
        public BaseSkillChoice buildChoice() {
            return new SkillChoice39(false, true, TankAttrType.ATK);
        }
    },
    SkillChoice40(40, "标记buff") {
        @Override
        public BaseSkillChoice buildChoice() {
            return new SkillChoice40(false);
        }
    },
    SkillChoice41(41, "标记buff") {
        @Override
        public BaseSkillChoice buildChoice() {
            return new SkillChoice40(true);
        }
    },
	SkillChoice42(42, "敌方随机1排#坦克数量") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SkillChoice42(false);
		}
	},
	SkillChoice43(43, "找最多坦克的一排") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SkillChoice43(false);
		}
	},
	SkillChoice44(44, "对方攻击力最高的和血量最少的目标") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SkillChoice44(false);
		}
	},
	SkillChoice45(45, "我的的上下左右") {
		@Override
		public BaseSkillChoice buildChoice() {
			return new SkillChoice45();
		}
	},
	;
	
	private SkillChoiceType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public static SkillChoiceType getType(int type) {
		for (SkillChoiceType buildType : SkillChoiceType.values()) {
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
	
	public abstract BaseSkillChoice buildChoice();
}
