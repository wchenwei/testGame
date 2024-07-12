package com.hm.war.sg;


import com.hm.enums.TankAttrType;
import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.ImmunityEvent;
import com.hm.war.sg.event.ShowAttrEvent;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

public class UnitAtkAction {
	public static final double HurtMinRate = 0.01;
	public static final double[] CritInfos = {2,1,10,-1};
	public static final double[] DogeInfos = {2,1,10,-1};
	public static final double[] SkillDogeInfos = {2,1,10,-1};
	public static final double MineAddSkillPer = -0.8d;//最小技能增强
	
	public static HurtBear calNormalAtkHurt(Frame frame, Unit atk, Unit def, boolean isCrit) {
		HurtBear normalHurt = atk.createNormalHurt(frame, atk, def);
		UnitAttr atkAttr = atk.getUnitAttr();
		UnitAttr defAttr = def.getUnitAttr();
		
		long atkHit = Math.max(atkAttr.getLongValue(TankAttrType.HIT), 0);
		long atkCrit = Math.max(atkAttr.getLongValue(TankAttrType.CRIT), 0);
		double atkCritDam = atkAttr.getDoubleValue(TankAttrType.CritDamPer);
		long atkAtk = atkAttr.getLongValue(TankAttrType.ATK);
		long atkAtkCd = atk.getSetting().getBaseAtkCd();
		double atkAddATK = atkAttr.getDoubleValue(TankAttrType.AddAtkPer);
		
		long defDodge = Math.max(defAttr.getLongValue(TankAttrType.DODGE), 0);
		long defCritDef = Math.max(defAttr.getLongValue(TankAttrType.CritDef), 0);
		double defCritRes = defAttr.getDoubleValue(TankAttrType.CritResPer);
		long defMaxHp = defAttr.getLongValue(TankAttrType.HP);
		long defDef = defAttr.getLongValue(TankAttrType.DEF);
		double defReduceATK = defAttr.getDoubleValue(TankAttrType.ReduceAtkPer);

//		double[] critInfos = StringUtil.splitStr2DoubleArray(SettingManager.getInstance().getStrValue(CommonValueType.FightCritRate), ",");
//		double[] dogeInfos = StringUtil.splitStr2DoubleArray(SettingManager.getInstance().getStrValue(CommonValueType.FightDogeRate), ",");
		double[] critInfos = {2,1,2,-1};
		double[] dogeInfos = {2,1,2,-1};
		//【伤害计算】闪避率计算系数，闪避率 = 参数1*闪避/（闪避+命中*参数2+参数3）+参数4，最小0.05，最大0.8，参数在common_value【385】
		//命中率 =max[0.2,  min[1,1 + （攻击方命中 - 防守方闪避）/1000]]         如果是技能 则命中率 = 1
		double dogeRate = 0;
		double dogeParm = defDodge+atkHit*dogeInfos[1]+dogeInfos[2];
		if(dogeParm > 0) {
			dogeRate = Math.max(0, Math.min(0.8, MathUtils.div(dogeInfos[0]*defDodge, dogeParm)+dogeInfos[3]));
		}
//		double hitRate = Math.max(0.2, Math.min(1, 1+MathUtils.div(atkHit-defDodge, 1000)));
		if(RandomUtils.randomIsRate(dogeRate)) {
			normalHurt.setAtkType(AtkAddType.Dodge);
			return normalHurt;
		}
		double normalRatio = 1;//攻击方普通攻击倍率系数
		//暴击率 =max[0, min[0.8 , (攻击方暴击 - 防守方防暴)/1000]
		//【伤害计算】暴击率计算系数，暴击率 = 参数1*暴击/（暴击+防暴*参数2+参数3）+参数4，最小0.05，最大0.8，参数在common_value【384】
		double critRate = 0;
		double critParm = atkCrit+defCritDef*critInfos[1]+critInfos[2];
		if(critParm > 0) {
			critRate = Math.max(0, Math.min(0.8, MathUtils.div(critInfos[0]*atkCrit, critParm)+critInfos[3]));
		}
		//暴伤率 = max[1 , 攻击方暴伤% - 防守方暴伤抵抗%] 如果没发生暴击 则 暴伤率 = 1
		double critDamRate = 1;
		if(isCrit || RandomUtils.randomIsRate(critRate)) {
			double bufVal = atk.getUnitBuffs().getBuffSumValue(UnitBufferType.DefCritResReduce);
			defCritRes = Math.max(0,(1-bufVal)*defCritRes);
			critDamRate =  Math.max(1,atkCritDam-defCritRes);
			normalHurt.setAtkType(AtkAddType.Crit);
		}
		//增伤率=max[-0.8, 攻击方增伤% - 防守方减伤%]
		double addHurtRate = Math.max(-0.8,atkAddATK-defReduceATK);
		//伤害修正
		double damageModify = SettingManager.getInstance().getDamageModify(atk.getSetting().getAmyType(), def.getSetting().getAmyType());
		//普通攻击伤害 =max[防守方耐久 x 3% ,  (攻击方攻击 - 防守方防御）X  攻击方攻击间隔 (帧数)/ 10帧 X 攻击方普通攻击倍率系数 X 暴伤率 X   (1 +增伤率）]
		double atkCdRate = MathUtils.div(atkAtkCd, 10);
		//最小伤害
		long minHurt = def.getSetting().isMinHurt()? MathUtils.mul(defMaxHp, HurtMinRate):0;
		long hurt = (long)Math.max(minHurt, MathUtils.mul((atkAtk-defDef),atkCdRate,normalRatio,critDamRate,1+addHurtRate,damageModify));
		hurt = Math.max(hurt, 1);
		normalHurt.setHurt(hurt);
		return normalHurt;
	}
	
	public static HurtBear calNormalAtkHurt(Frame frame, Unit atk, Unit def) {
		return calNormalAtkHurt(frame, atk, def, false);
	}
	public static long calSkillHurt(Unit atk, Unit def, double skillHurt) {
		return calSkillHurt(atk, def, skillHurt, true);
	}

    public static long calSkillHurt(Unit atk, Unit def, double skillHurt, boolean haveMinHurt) {
        return calSkillHurt(atk, def, skillHurt, haveMinHurt, true);
    }
	
	//技能攻击伤害 = max[防守方耐久 x 3%， 各自技能对应伤害公式的结果 x（1+ 攻击方技能增强%）X 暴伤率 X （1 + 增伤率）]
    public static long calSkillHurt(Unit atk, Unit def, double skillHurt, boolean haveMinHurt, boolean haveDefReduceATK) {
		if(def.getUnitBuffs().isTriggerRateBuff(UnitBufferType.RateNoSkillHurt)) {
			def.getMyGroup().addEvent(new ImmunityEvent(def));
			return 0;
		}
		UnitAttr atkAttr = atk.getUnitAttr();
		UnitAttr defAttr = def.getUnitAttr();
		double atkAddATK = atkAttr.getDoubleValue(TankAttrType.AddAtkPer);
		double skillHurtRate = getAddSkillPer(atkAttr);
		
		long defMaxHp = defAttr.getLongValue(TankAttrType.HP);
		double defReduceATK = defAttr.getDoubleValue(TankAttrType.ReduceAtkPer);
		double defSkillHurtReduce = defAttr.getDoubleValue(TankAttrType.SkillHurtReduce);
//		double[] critInfos = StringUtil.splitStr2DoubleArray(SettingManager.getInstance().getStrValue(CommonValueType.FightCritRate), ",");
		//暴伤率 = max[1 , 攻击方暴伤% - 防守方暴伤抵抗%] 如果没发生暴击 则 暴伤率 = 1
		double critDamRate = 1;
		//【伤害计算】暴击率计算系数，暴击率 = 参数1*暴击/（暴击+防暴*参数2+参数3）+参数4，最小0.05，最大0.8，参数在common_value【384】
//		double critRate = 0;
//		double critParm = atkCrit+defCritDef*critInfos[1]+critInfos[2];
//		if(critParm > 0) {
//			critRate = Math.max(0, Math.min(0.8, MathUtils.div(critInfos[0]*atkCrit, critParm)+critInfos[3]));
//		}
		//暴击率 =max[0, min[0.8 , (攻击方暴击 - 防守方防暴)/1000]
//		double critRate = Math.max(0, Math.min(0.8, MathUtils.div(atkCrit-defCritDef, 1000)));
		//TODO 技能暴击占时不生效
//		if(RandomUtils.randomIsRate(critRate)) {
//			critDamRate =  Math.max(1, atkCritDam-defCritRes);
//		}
        double addHurtRate = atkAddATK;
        if (haveDefReduceATK) {
            //增伤率=max[-0.8, 攻击方增伤% - 防守方减伤%]
            addHurtRate = Math.max(-0.8, atkAddATK - defReduceATK);
        }
		//伤害修正
		double damageModify = SettingManager.getInstance().getDamageModify(atk.getSetting().getAmyType(), def.getSetting().getAmyType());
		//最小伤害
		long hurt = (long)MathUtils.mul(skillHurt,(1+skillHurtRate),critDamRate,1+addHurtRate,damageModify);
		if(defSkillHurtReduce > 0) {//技能伤害减免
			hurt = (long)(hurt*(1-defSkillHurtReduce));
		}
		if(haveMinHurt) {
			long minHurt = def.getSetting().isMinHurt()? MathUtils.mul(defMaxHp, HurtMinRate):0;
			//max[防守方耐久 x 3%， 各自技能对应伤害公式的结果 x（1+ 攻击方技能增强%）X 暴伤率 X （1 + 增伤率）]
			hurt = (long)Math.max(minHurt,hurt);
		}
		return Math.max(hurt, 1);
	}
	
	//伤害类BUFF 每次伤害值 = 对应技能BUFF每次的伤害值 X (1 + 释放BUFF者的技能增强% ）
	public static double calBuff(Unit atk, Unit def, double skillHurt) {
		UnitAttr atkAttr = atk.getUnitAttrNoSkill();
		double skillAdd = getAddSkillPer(atkAttr);
		double hurt = MathUtils.mul(skillHurt,1+skillAdd);
		return hurt;
	}
	
	//治疗 值 X （1+治疗释放者技能增强% + 受治疗者的被治疗增强%）
	public static double calCure(Unit atk, Unit def, double skillHurt) {
		UnitAttr atkAttr = atk.getUnitAttrNoSkill();
		UnitAttr defAttr = def.getUnitAttrNoSkill();
		double skillAdd = getAddSkillPer(atkAttr);
		double defAdd = defAttr.getDoubleValue(TankAttrType.AddCurePer);//被治疗增强
		double hurt = MathUtils.mul(skillHurt,1+skillAdd+defAdd);
		return Math.max(hurt, 0);
	}
	
	public static double getAddSkillPer(UnitAttr atkAttr) {
		return Math.max(atkAttr.getDoubleValue(TankAttrType.AddSkillPer), MineAddSkillPer);
	}
}
