package com.hm.war.sg.cal;

import com.hm.enums.TankAttrType;

/**
 * @Description: 计算公式类型
 * @author siyunlong  
 * @date 2018年9月27日 下午4:43:06 
 * @version V1.0
 */
public enum CalFormulaType {
	Formula0(0,"None") {
		@Override
		public BaseCalFormula buildFormula() {
			return new NoneFormula();
		}
	},
	Formula1(1,"释放者攻击力-目标防御力）*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new HurtFormula();
		}
	},
	Formula2(2,"释放者攻击力*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new NoDefHurtFormula();
		}
	},
	Formula3(3,"（目标血量上限-目标剩余血量）*系数A+系数B	目标损失血量伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new DefLoseHpHurtFormula();
		}
	},
	Formula4(4,"目标血量上限*系数A	血量上限伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.HP,false);
		}
	},
	Formula5(5,"目标防御力*系数A+系数B	目标防御力伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new DefDefHurtFormula();
		}
	},
	Formula6(6,"释放者攻击力*系数AB	持续伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.ATK,true);
		}
	},
	Formula7(7,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AddHpFormula();
		}
	},
	Formula8(8,"受到的普攻伤害*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AddHpFormula();
		}
	},
	Formula9(9,"目标当前能量*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula9();
		}
	},
	Formula10(10,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula10();
		}
	},
	Formula11(11,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AddHpFormula();
		}
	},
	Formula12(12,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula12();
		}
	},
	Formula13(13,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AddHpFormula();
		}
	},
	Formula14(14,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula14();
		}
	},
	Formula15(15,"系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AddHpFormula();
		}
	},
	Formula16(16,"【int（（释放者血量上限-剩余血量）/（施放者血量上限*0.1））】*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula16();
		}
	},
	Formula17(17,"释放者当前命中*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.HIT,true);
		}
	},
	Formula18(18,"释放者当前闪避*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.DODGE,true);
		}
	},
	Formula19(19,"自己能量上限*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula19();
		}
	},
	Formula20(20,"释放者防御力*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.DEF,true);
		}
	},
	Formula21(21,"目标防御力*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.DEF,false);
		}
	},
	Formula22(22,"目标攻击力*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.ATK,false);
		}
	},
	Formula23(23," 【int（当前血量/（自己能量上限*0.1））】*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula23();
		}
	},
	Formula24(24,"目标能量上限*系数a") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula24();
		}
	},
	Formula25(25,"己方存活战车攻击总和*系数A+系数B加两个计算类型") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula25();
		}
	},
	Formula26(26,"释放者攻击CD*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.AtkCd,true);
		}
	},
	Formula27(27,"目标攻击CD*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.AtkCd,false);
		}
	},
	Formula28(28,"(目标血量上限-目标剩余血量）*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula28();
		}
	},
	Formula29(29,"目标当前闪避*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.DODGE,false);
		}
	},
	
	Formula30(30,"（（释放者攻击力-目标防御力）*系数A+系数B）*施放者暴击伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula30();
		}
	},
	Formula31(31,"（释放者攻击力*系数A+系数B）*敌方当前数量") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula31();
		}
	},
	Formula32(32,"目标攻击力*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormulaAndB(TankAttrType.ATK,false);
		}
	},
	Formula33(33,"目标当前血量*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new HpCalFormulaAndB(false);
		}
	},
	Formula34(34,"3.6471*ln(攻速提升百分比) + 10.365") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula34();
		}
	},
	Formula35(35,"58.128*(攻速降低百分比)^2 + 8.9686*(攻速降低百分比)") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula35();
		}
	},
	Formula36(36,"目标血量上限*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormulaAndB(TankAttrType.HP,false);
		}
	},
	Formula37(37,"目标当前命中*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.HIT,false);
		}
	},
	Formula38(38,"（（释放者攻击力-目标防御力）*系数A+系数B）*（施放者暴击伤害-当前目标暴伤抗性（最小取2））") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula38();
		}
	},
	Formula39(39,"目标当前暴击*系数A") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormula(TankAttrType.CRIT,false);
		}
	},
	Formula40(40,"释放者防御力*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new AttrCalFormulaAndB(TankAttrType.DEF,true);
		}
	},
	Formula41(41,"释放者当前攻击力-目标当前防御力*0.5）*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula41();
		}
	},
	Formula42(42,"目标当前抗暴*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
            return new AttrCalFormulaAndB(TankAttrType.CritDef, false);
		}
	},
	Formula43(43,"（释放者攻击力-目标防御力）*（系数A+敌军数量（最大为5））+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula43();
		}
	},
	Formula44(44,"释放者进场防御力*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new IntoWarAttrCalFormulaAndB(TankAttrType.DEF,true);
		}
	},
	Formula45(45,"释放者进场攻击力*系数A+系数B") {
		@Override
		public BaseCalFormula buildFormula() {
			return new IntoWarAttrCalFormulaAndB(TankAttrType.ATK,true);
		}
	},
    Formula46(46, "全场队友抗暴*系数A+系数B") {
        @Override
        public BaseCalFormula buildFormula() {
            return new AllUnitAttrCalFormula(TankAttrType.CritDef, true);
        }
    },
    Formula47(47, "除了前排外的队友抗暴*系数A+系数B") {
        @Override
        public BaseCalFormula buildFormula() {
            return new AllUnitNotFirstAttrCalFormula(TankAttrType.CritDef, true);
        }
    },
    Formula48(48, "目标防御力*系数A*场上存活队友数量") {
        @Override
        public BaseCalFormula buildFormula() {
            return new Formula48(false, TankAttrType.DEF);
        }
    },
	Formula50(50, "（己方存活战车攻击总和*系数A+系数B）*（1-飞机减伤）") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula50();
		}
	},
    Formula51(51, "（释放者攻击力-目标防御力）") {
        @Override
        public BaseCalFormula buildFormula() {
            return new Formula51();
        }
    },
    Formula52(52, "（释放者攻击力-目标防御力）") {
        @Override
        public BaseCalFormula buildFormula() {
            return new Formula52();
        }
    },
	Formula53(53, "（释放者攻击力-目标防御力）") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula53();
		}
	},
	Formula54(54, "（目标攻击力-目标防御力）") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula54();
		}
	},
	Formula55(55, "指挥官技能-真实伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula55(0);
		}
	},
	Formula56(56, "指挥官技能-真实伤害") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula55(1);
		}
	},
	Formula57(57,"释放者当前攻击力-目标当前防御力*系数A）*系数B+系数C") {
		@Override
		public BaseCalFormula buildFormula() {
			return new Formula57();
		}
	},
	;
	private CalFormulaType(int type,String desc) {
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public static CalFormulaType getType(int type) {
		for (CalFormulaType buildType : CalFormulaType.values()) {
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
	
	public abstract BaseCalFormula buildFormula();
}
