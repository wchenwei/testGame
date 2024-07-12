package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.buff.BuffKind;
import com.hm.war.sg.buff.UnitBufferType;

public class EffectBuilderFactory {
	public static BaseSkillEffect create(int id) {
		switch (id) {
			case 1: return new HurtEffect(AtkAddType.None);
			case 2: return new AddAttrBufferEffect(TankAttrType.ATK);
			case 3: return new ReduceAttrBufferEffect(TankAttrType.ATK);
			case 4: return new AddAttrBufferEffect(TankAttrType.DEF);
			case 5: return new ReduceAttrBufferEffect(TankAttrType.DEF);
//			case 6: return new AddAttrBufferEffect(TankAttrType.HP);
//			case 7: return new ReduceAttrBufferEffect(TankAttrType.HP);
//			case 8: return new AddAttrBufferEffect(TankAttrType.HP);
//			case 9: return new ReduceAttrBufferEffect(TankAttrType.HP);
			case 10: return new AddAttrBufferEffect(TankAttrType.HIT);
			case 11: return new ReduceAttrBufferEffect(TankAttrType.HIT);
			case 12: return new AddAttrBufferEffect(TankAttrType.DODGE);
			case 13: return new ReduceAttrBufferEffect(TankAttrType.DODGE);
			case 14: return new AddAttrBufferEffect(TankAttrType.CRIT);
			case 15: return new ReduceAttrBufferEffect(TankAttrType.CRIT);
			case 16: return new AddAttrBufferEffect(TankAttrType.CritDef);
			case 17: return new ReduceAttrBufferEffect(TankAttrType.CritDef);
			case 18: return new AddAttrBufferEffect(TankAttrType.CritDamPer);
			case 19: return new ReduceAttrBufferEffect(TankAttrType.CritDamPer);
			case 20: return new AddAttrBufferEffect(TankAttrType.CritResPer);
			case 21: return new ReduceAttrBufferEffect(TankAttrType.CritResPer);
			case 22: return new AddAttrBufferEffect(TankAttrType.AddAtkPer);
			case 23: return new ReduceAttrBufferEffect(TankAttrType.AddAtkPer);
			case 24: return new AddAttrBufferEffect(TankAttrType.ReduceAtkPer);
			case 25: return new ReduceAttrBufferEffect(TankAttrType.ReduceAtkPer);
			case 26: return new AddAttrBufferEffect(TankAttrType.AtkCd);//减少攻速
			case 27: return new ReduceAttrBufferEffect(TankAttrType.AtkCd);//增加攻速
			case 28: return new BuffEffect(UnitBufferType.StunBuff);
			case 29: return new BuffEffect(UnitBufferType.SilentBuff);
			case 30: return new AddHpEffect();
			case 31: return new ContinueRecoverHpEffect(true);
			case 32: return new ContinueRecoverHpEffect(false);
			case 33: return new ReduceHurtEffect();
			case 34: return new AddHurtEffect();
			case 35: return new ManyHurtEffect(AtkAddType.None);
			case 36: return new ClearBuffEffect(BuffKind.DeBuff);
			case 37: return new ClearBuffEffect(BuffKind.Buff);
			case 38: return new BuffEffect(UnitBufferType.MarkBuff);
			case 40: return new BuffValueEffect(UnitBufferType.HurtEffectBuff,true);
			case 41: return new BuffValueEffect(UnitBufferType.HurtEffectBuff,false);
			case 42: return new AddMpEffect(true);
			case 43: return new AddMpEffect(false);
			case 44: return new BuffEffect(UnitBufferType.NotStunBuff);
			case 45: return new OfferHpEffect();
			case 46: return new CritEffect();
			case 47: return new DoubleHitEffect();
			case 48: return new BuffValueEffect(UnitBufferType.CureEffectBuff,false);
			case 49: return new BuffValueEffect(UnitBufferType.CureEffectBuff,true);
			case 50: return new DrawEffect(0);//吸血
			case 51: return new DrawEffect(1);//吸蓝
			case 52: return new BuffValueEffect(UnitBufferType.ShieldBuff,true);
			case 53: return new NormalAtkAddHpEffect();
			case 54: return new ReviveEffect();
			case 55: return new BuffEffect(UnitBufferType.NoDeath);
			case 56: return new BuffEffect(UnitBufferType.InvincibleBuff);
			case 57: return new BuffValueEffect(UnitBufferType.AtkSuckBlood,true);
			case 58: return new BackHurtEffect();
			case 59: return new BuffValueEffect(UnitBufferType.BackHurtBuff,true);
			case 60: return new BuffValueEffect(UnitBufferType.AddAtkTargets,true);
			case 61: return new ContinueRecoverMpEffect(true);
			case 62: return new ContinueRecoverMpEffect(false);
			case 63: return new BuffEffect(UnitBufferType.NotReviveBuff);
			case 64: return new HurtLinkEffect();
			case 65: return new ManyRandomHurtEffect();
			case 66: return new BuffValueEffect(UnitBufferType.RateDodgeBuff,true);
			case 67: return new BuffValueEffect(UnitBufferType.RateNotStunBuff,true);
			case 68: return new AddAttrBufferEffect(TankAttrType.MpRecover);
			case 69: return new ReduceAttrBufferEffect(TankAttrType.MpRecover);
			case 70: return new RateBuffEffect(UnitBufferType.StunBuff);
			case 71: return new AttrIntervalChangeBuffEffect(TankAttrType.ATK);
			case 72: return new AttrIntervalChangeBuffEffect(TankAttrType.DEF);
			case 73: return new AttrIntervalChangeBuffEffect(TankAttrType.ReduceAtkPer);
			case 74: return new ClearOneBuffEffect(UnitBufferType.ShieldBuff);
			case 75: return new HurtEffect(AtkAddType.Crit);
			case 76: return new ManyHurtEffect(AtkAddType.Crit);
			case 77: return new AddExtraSkillEffect();
			case 78: return new BuffEffect(UnitBufferType.TauntBuff);
			case 79: return new BuffEffect(UnitBufferType.NoAtkBuff);
			case 80: return new BuffEffect(UnitBufferType.NextDeathBuff);
			case 81: return new BuffEffect(UnitBufferType.SmallSkillSilentBuff);
			case 82: return new BuffValueEffect(UnitBufferType.RandomAddAtkTargets,true,1);
			case 83: return new StealAttrEffect(TankAttrType.HIT);
			case 84: return new BuffValueEffect(UnitBufferType.DodgeBuff,true);
			case 85: return new BuffValueEffect(UnitBufferType.RateNoSilentBuff,true);
			//没有暴击时增加暴击属性
			case 86: return new AddAttrBufferForNormalAtkEffect(TankAttrType.CRIT,AtkAddType.Crit,false);
			//87  能量无法增加
			case 87: return new BuffEffect(UnitBufferType.NotAddMpBuff);
			case 88: return new BuffEffect(UnitBufferType.NotAddMpSkillBuff);
			case 89: return new BeheadedEffect();
			case 90: return new BuffValueEffect(UnitBufferType.HurtHp10EffectBuff,false);
			//破裂护盾
			case 91: return new BuffValueEffect(UnitBufferType.ShieldBuffBackHp,true,1d);
			case 92: return new BuffValueEffect(UnitBufferType.HurtSelfLink,true);
			case 93: return new BuffValueEffect(UnitBufferType.ReduceMpBuff,false);
			case 94: return new BuffValueEffect(UnitBufferType.ReduceStunBuff,true);
			case 95: return new ResetNormalCDEffect();
			case 96: return new BuffValueEffect(UnitBufferType.AtkDodgeBuff,true);
			case 97: return new BuffValueEffect(UnitBufferType.AtkDodgeBuff,false);
			case 98: return new ClearOneBuffEffect(UnitBufferType.InvincibleBuff);
			case 99: return new HurtLinkDeathEffect();
			case 100: return new ClearMarkBuffEffect();
			case 101: return new BuffEffect(UnitBufferType.FixedBodyBuff);
            case 102:
                return new BuffValueEffect(UnitBufferType.ShieldBuffAddMp, true, 0.1d);
			case 103: return new BuffValueEffect(UnitBufferType.ReduceMpBuff,true);
			case 104: return new BuffEffect(UnitBufferType.NoShieldBuff);
			case 105: return new AddAttrBufferEffect(TankAttrType.AddSkillPer);
			case 106: return new ReduceAttrBufferEffect(TankAttrType.AddSkillPer);
			case 107: return new BuffValueEffect(UnitBufferType.HurtHp80EffectBuff,false);
			case 108: return new BuffValueEffect(UnitBufferType.ShieldBuffBackHp,true,0.2d);
            case 109:
                return new ClearOneBuffEffect(UnitBufferType.SilentBuff);
            case 110:
                return new ClearOneBuffEffect(UnitBufferType.StunBuff);
            case 111:
                return new BuffValueEffect(UnitBufferType.ShieldBuffAddMp, true, 0.4d);
			case 112:
				return new BuffValueEffect(UnitBufferType.TransHurtBuff, true, 30);
			case 113:
				return new ClearOneBuffEffect(UnitBufferType.ShedBloodBuff, true);
			case 114:
				return new AddAircraftOilEffect();
			case 115:
				return new BuffValueEffect(UnitBufferType.TransHurtBuff, true, 10);
			case 116:
				return new BuffValueEffect(UnitBufferType.DefCritResReduce,true);
			case 117: return new AddAttrBufferEffect(TankAttrType.SkillHurtReduce);
			case 118: return new ReduceAttrBufferEffect(TankAttrType.SkillHurtReduce);
			case 119:
				return new BuffValueEffect(UnitBufferType.RateNoSkillHurt,true);
			case 120:
				return new BuffValueEffect(UnitBufferType.RateNoReduceMP,true);
			case 121: return new HurtEffect(AtkAddType.None).setIgnoreShield(true);
			case 122: return new StealBuffEffect(52);
			case 123: return new BuffValueEffect(UnitBufferType.ShieldBuff,true);
			case 125:
				return new BuffValueEffect(UnitBufferType.RateNoFixedBody,true);
			case 126:
				return new ContinueRecoverHpEffect2(false);
			case 127:
				return new HurtShareEffect();
			case 128: return new HurtEffect(AtkAddType.HolyHurt);

			case 129: return new AddAttrBufferEffect(TankAttrType.FinalAddAtkPer);
			case 130: return new ReduceAttrBufferEffect(TankAttrType.FinalAddAtkPer);
			case 131: return new AddAttrBufferEffect(TankAttrType.FinalReduceAtkPer);
			case 132: return new ReduceAttrBufferEffect(TankAttrType.FinalReduceAtkPer);
		}
		System.err.println("找不到BaseSkillEffect："+id);
		return null;
	}
}
