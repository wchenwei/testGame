package com.hm.war.sg.unit;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.guildwar.WarConstants;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;
import com.hm.war.sg.*;
import com.hm.war.sg.bear.*;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.SkillPreBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.buff.UnitBuffs;
import com.hm.war.sg.event.DeathEvent;
import com.hm.war.sg.event.DropItemEvent;
import com.hm.war.sg.event.NormalAtkEvent;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillType;
import com.hm.war.sg.skillnew.UnitSkills;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 战斗单元
 * @author siyunlong  
 * @date 2018年9月6日 下午2:54:34 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class Unit {
	int id;//id 位置绑定的id
	int index;//位置
	protected UnitSetting setting;//参数配置
	protected StatisticsEngine statisticsEngine;//统计引擎
	
	@Transient
	protected transient UnitGroup myGroup;
	@Transient
	protected transient UnitGroup defGroup;
	@Transient
	protected transient HpEngine hpEngine;//hp管理引擎
	@Transient
	protected transient MpEngine mpEngine;//mp管理引擎
	@Transient
	protected transient AtkEngine atkEngine;//atk管理引擎
	@Transient
	protected transient BearEngine bearEngine;//bear管理引擎
	@Transient
	protected transient UnitSkills unitSkills;//技能列表
	@Transient
	protected transient UnitBuffs unitBuffs;//buff信息
    @Transient
    protected transient UnitAttr intoWarUnitAttr;
    @Transient
    protected transient UnitAttr noSkillUnitAttr;
    @Transient
    protected transient UnitAttr allUnitAttr;
    @Transient
    protected transient Unit curAtkMe;//当前正在打我的人

	public void fight(Frame frame) {
		long curFrame = frame.getId();
		if (isDeath()) {
			return;
		}
		//==========计算收到的伤害===============
		this.unitSkills.checkExtraSkill(frame);
		this.bearEngine.calUnitHurt(frame);
		if(isDeath()) {
			return;
		}
		//计算蓝量回复
		this.mpEngine.calLastRecoveMpFrame(frame);
		//==========计算收到的伤害===============
		unitBuffs.checkBuffOver(frame);//检查buffer是否已经结束
        fightLoadPre();//重置属性
		//每帧技能触发
		getUnitSkills().doReleaseSkillForType(frame, SkillType.FrameSkill);

		if(unitBuffs.haveBuff(WarComm.NoAtkBuffList)) {
			return;//处于眩晕状态
		}
		//检查技能前摇是否到时见
		SkillPreBuff skillPre = unitBuffs.getSkillPre();
		if(skillPre != null) {
			if(skillPre.isOver(curFrame)) {//前摇完成了，释放
				unitBuffs.setSkillPre(null);
				unitSkills.doActiveSkill(frame, this, myGroup, defGroup, skillPre.getSkillId());
			}
			return;//前摇未完成继续等待
		}
		boolean isCanRealseSkill = isCanRealseSkill();//是否可以释放主动技能
		//计算是否可以释放技能
		if(isCanRealseSkill && unitSkills.doPreActiveSkill(frame, this)) {
			return;//释放前摇技能了
		}
		if(!this.atkEngine.isCanAtk(curFrame)) {
			return;//正在攻击cd
		}
		//重置上次攻击
		this.atkEngine.updateLastFrame(curFrame);
		//判断是否触发小技能
		if(isCanRealseSkill && unitSkills.isPreSmallTriggerSkill(frame, this)) {
			return;//释放前摇技能了
		}
		try {
			//普通攻击
			doNormalAtk(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void fightLoadPre() {
        this.noSkillUnitAttr = null;
        this.allUnitAttr = null;
    }


	public boolean isNormalAtkDodge() {
		double mpRate = getUnitBuffs().getBuffSumValue(UnitBufferType.AtkDodgeBuff);
		return mpRate > 0 && RandomUtils.randomIsRate(mpRate);
	}
	
	public void doNormalAtk(Frame frame) {
		List<Unit> defTargets = Lists.newArrayList(this.atkEngine.choiceTarget(defGroup));//选择目标普通攻击
		this.getAtkEngine().addNormalAtkNum();
		getUnitSkills().doReleaseSkillForType(frame, SkillType.NormalAtk);
		boolean isAtkDodge = isNormalAtkDodge();//是否普攻miss
		List<Integer> defIDList = defTargets.stream().map(e -> e.getId()).collect(Collectors.toList());

		for (int i = 0; i < defTargets.size(); i++) {
			Unit target = defTargets.get(i);
			HurtBear normalHurt = UnitAtkAction.calNormalAtkHurt(frame, this, target);
			if(isAtkDodge) {
				normalHurt.setHurt(0);
				normalHurt.setAtkType(AtkAddType.Dodge);
			}
			if(i == 0) {
				frame.addEvent(new NormalAtkEvent(this, defIDList,normalHurt.getEffectFrame() - frame.getId()));
			}
			target.addBear(frame,normalHurt);
			//判断是否可闪避
			if(normalHurt.getHurt() > 0 && target.getUnitBuffs().isDodgeNormalAtk()) {
				normalHurt.setHurt(0);
				normalHurt.setAtkType(AtkAddType.Dodge);
			}
			if(normalHurt.getHurt() <= 0) {
				//触发被闪避技能
				getUnitSkills().doReleaseSkillForType(frame, SkillType.AtkBeDodge);
				//触发我的闪避技能
				target.getUnitSkills().doReleaseSkillForType(frame, SkillType.AtkDodge);
				return;
			}
			target.setCurAtkMe(this);//设置当前正在打我的人
			//先算攻击方加成
			//算防守方的防守减伤和闪避 ->
			//攻击方产生buff和本攻击产生的吸血
			//防守方的手机反伤+buff
			List<Skill> atkPassiveSkill = getUnitSkills().getSkillListByType(frame,SkillType.PassiveAtkSkill1);
			List<Skill> defPassiveSkill = target.getUnitSkills().getSkillListByType(frame,SkillType.PassiveDefSkill1);
			//防御方技能触发
			defPassiveSkill.stream().forEach(e -> e.doPassiveDefSkill(frame,this, target, normalHurt));
			//攻击方技能触发
			atkPassiveSkill.stream().forEach(e -> e.doPassiveAtkSkill(frame,this, target, normalHurt));
			//收到普工伤害触发技能
			target.getUnitSkills().doReleaseSkillForType(frame, SkillType.BeNormalAtkHurt);

			//判断暴击触发
			if(normalHurt.getAddType() == AtkAddType.Crit.getType()) {
//				getUnitSkills().getSkillListByType(frame,SkillType.AtkCritSkill)
//				.forEach(e -> e.doPassiveAtkSkill(frame,this, target, normalHurt));
				getUnitSkills().doReleaseSkillForType(frame, SkillType.AtkCritSkill);
				target.getUnitSkills().doReleaseSkillForType(frame, SkillType.DefCritSkill);
			}
			//判断攻击者吸血buff
			double suckBloodRate = getUnitBuffs().getBuffSumValue(UnitBufferType.AtkSuckBlood);
			if(suckBloodRate > 0) {
				BaseBuffer buff = getUnitBuffs().getFirstBaseBufferByType(UnitBufferType.AtkSuckBlood);
				int funcId = buff == null ? 0:buff.getFuncId();
				long addHp = MathUtils.mul(normalHurt.getHurt(), suckBloodRate);
				addBear(frame,new RecoverBear(getId(), addHp, normalHurt.getEffectFrame(),funcId));
			}
			//判断防御者反伤
			if(target.getUnitBuffs().isCanBackHurt()) {//是否可以反伤
				double backHurtRate = target.getUnitBuffs().getBuffSumValue(UnitBufferType.BackHurtBuff);
				if(backHurtRate > 0) {
					long backHurt = MathUtils.mul(normalHurt.getHurt(), backHurtRate);
					//计算减伤
					backHurt = UnitAtkAction.calSkillHurt(target, this, backHurt,false);
					backHurt = Math.min(backHurt, target.getHp());
					HurtBear hurtBear = new HurtBear(target.getId(), normalHurt.getEffectFrame(), backHurt, 0, 0);
					hurtBear.setAddType(AtkAddType.BackHurt.getType());
					addBear(frame, hurtBear);
				}
			}
		}
	}
	
	public HurtBear createNormalHurt(Frame frame, Unit atk, Unit def) {
		HurtBear hurtBear = new HurtBear(this.id, frame.getId()+this.setting.getBulletFlyFrame(atk,def), 0,0,0);
		hurtBear.setAtkTimes(getSetting().getAtkTimes());
		return hurtBear;
	}
	public boolean isDeath() {
		return this.hpEngine.isDeath();
	}
	public void addBear(Frame frame, Bear bear) {
		this.bearEngine.addBear(frame,bear);
	}
	public void addBuffer(BaseBuffer buff) {
		this.unitBuffs.addBuff(buff);
	}
	/**
	 * 处理战车死亡
	 * @param atkId
	 */
	public void doUnitDeath(Frame frame,int atkId) {
		//释放复活技能
		this.myGroup.doReviveSkill(frame, this);
		if(!isDeath()) {
			return;
		}
		//自己死亡触发
		getUnitSkills().doReleaseSkillForType(frame, SkillType.MyDeathSkill);
		//触发死亡技能
		getUnitSkills().doReleaseSkillForType(frame, SkillType.SelfDeathSkill);
		getMyGroup().doReleaseSkillForType(frame, SkillType.SelfDeathSkill);
		//触发敌军死亡触发技能
		getDefGroup().doReleaseSkillForType(frame, SkillType.DefDeathSkill);
		//死亡后处理，队友的buff
		getDefGroup().getUnits().forEach(e -> e.getUnitBuffs().doUnitDeathForMe(frame,getId()));
		
		//删除队友身上的buff
		getUnitSkills().doUnitDeath(frame,this);
		//死亡事件
		frame.addEvent(new DeathEvent(getId()));
		
		Unit atkUnit = getDefGroup().getUnitById(atkId);
		if(atkUnit == null) {
			//找不到攻击者-随机处理
			List<Unit> lifeUnits = getDefGroup().getLifeUnit();
			if(CollUtil.isNotEmpty(lifeUnits)) {
				atkUnit = RandomUtils.randomEle(lifeUnits);
			}
		}
		if(atkUnit != null) {
			atkUnit.doKillUnit(frame, this);
		}
	}
	
	/**
	 * 处理杀死敌人
	 * @param frame
	 * @param deathUnit
	 */
	public void doKillUnit(Frame frame,Unit deathUnit) {
		double addMp = SettingManager.getInstance().getKillAddMp();
		addBear(frame, new RecoverMpBear(getId(), addMp, frame.getId(), 0));
		//触发杀敌技能
		getUnitSkills().doReleaseSkillForType(frame, SkillType.KillTankDoSkill);
		//记录统计
		this.statisticsEngine.addKillTank(deathUnit.getSetting().getId());
		//掉落
		List<Items> dropItems = deathUnit.getSetting().getDropItems();
		if(CollUtil.isNotEmpty(dropItems) && getMyGroup().getFightTroopType() == FightTroopType.Player) {
			frame.addEvent(new DropItemEvent(deathUnit.getId(), dropItems));//添加事件
			this.myGroup.addDropItems(dropItems);
		}
	}
	
	public Unit(UnitSetting setting) {
		this.setting = setting;
		this.id = setting.getId();
		this.hpEngine = new HpEngine(this);
		this.mpEngine = new MpEngine(this);
		this.atkEngine = new AtkEngine(this);
		this.bearEngine = new BearEngine(this);
		this.unitBuffs = new UnitBuffs(this);
		this.unitSkills = new UnitSkills(this);
		this.statisticsEngine = new StatisticsEngine();
	} 
	public Unit(int id) {
		this.id = id;
	} 
	public void loadGroup(UnitGroup myGroup,UnitGroup defGroup) {
		this.myGroup = myGroup;
		this.defGroup = defGroup;
	}
	public int getId() {
		return id;
	}
	public double getHpRate() {
		return this.hpEngine.getHpRate();
	}
	public long getHp() {
		return this.hpEngine.getHp();
	}
	public double getMpRate() {
		return this.mpEngine.getMpRate();
	}
	public double getCurMp() {
		return this.mpEngine.getMp();
	}
	public long getBaseAtk() {
		return this.setting.getUnitAttr().getLongValue(TankAttrType.ATK);
	}
	public long getCurAtk() {
		return this.getUnitAttr().getLongValue(TankAttrType.ATK);
	}
	
	public long getMaxHp() {
		long maxHp = getSetting().getMaxHp();
		if(maxHp > 0) {
			return maxHp;
		}
		return this.setting.getUnitAttr().getLongValue(TankAttrType.HP);
	}
	
	public UnitAttr getUnitAttr() {
        if (WarConstants.WarAttrStayFrame && this.allUnitAttr != null) {
            return allUnitAttr;
        }
        UnitAttr unitAttr = getUnitAttrNoSkill().clone();
		//计算技能加成
		unitAttr.addAttrMap(getUnitSkills().calHpUnitAttr());
        this.allUnitAttr = unitAttr;
		return unitAttr;
	}
	
	//技能影响之外的属性
	public UnitAttr getUnitAttrNoSkill() {
        if (WarConstants.WarAttrStayFrame && this.noSkillUnitAttr != null) {
            return noSkillUnitAttr;
        }
        UnitAttr unitAttr = getIntoWarUnitAttr().clone();
		//计算buff产生的加成
		Map<TankAttrType,Double> buffAdd = this.unitBuffs.calBuffUnitAttr();
		unitAttr.addAttrMapForEnum(buffAdd);
        this.noSkillUnitAttr = unitAttr;
		return unitAttr;
	}
	public UnitAttr getIntoWarUnitAttr() {
        if (WarConstants.WarAttrStayFrame && this.intoWarUnitAttr != null) {
            return intoWarUnitAttr;
        }

		UnitAttr unitAttr = this.setting.getUnitAttr().clone();
		double atkDefRate = this.myGroup.getReduceAtkDefRate();
		if(atkDefRate < 1) {
			unitAttr.putAttr(TankAttrType.ATK, Math.max(unitAttr.getDoubleValue(TankAttrType.ATK)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.DEF, Math.max(unitAttr.getDoubleValue(TankAttrType.DEF)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.HIT, Math.max(unitAttr.getDoubleValue(TankAttrType.HIT)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.DODGE, Math.max(unitAttr.getDoubleValue(TankAttrType.DODGE)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.CRIT, Math.max(unitAttr.getDoubleValue(TankAttrType.CRIT)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.CritDef, Math.max(unitAttr.getDoubleValue(TankAttrType.CritDef)*atkDefRate, 1));
			unitAttr.putAttr(TankAttrType.AddAtkPer, Math.max(unitAttr.getDoubleValue(TankAttrType.AddAtkPer)*atkDefRate, 0));
			unitAttr.putAttr(TankAttrType.ReduceAtkPer, Math.max(unitAttr.getDoubleValue(TankAttrType.ReduceAtkPer)*atkDefRate, 0));
		}
        this.intoWarUnitAttr = unitAttr;
		return unitAttr;
	}
	
	public void calMaxHp() {
		long maxHp = this.setting.getUnitAttr().getLongValue(TankAttrType.HP);
//		maxHp += this.unitBuffs.calBuffMaxHp();
		if(maxHp <= 0) {
			maxHp = 1;
		}
		getSetting().setMaxHp(maxHp);
		if(this.setting.isFullHp()) {
			getSetting().setInitHp(maxHp);
			getHpEngine().setInitHp(maxHp);
		}else{
			getHpEngine().addHp(0);//保证当前血量<=最大血量
		}
	}
	
	//是否可以释放主动技能 1 5 12
	public boolean isCanRealseSkill() {
		return  myGroup.isCanReleaseSkill() && !unitBuffs.isNotRealseSkill();
	}


	//计算承受伤害
	public void calBearHurt(int atkId, long hurt) {
		if(atkId >= 0 && this.defGroup != null) {
			Unit atk = this.defGroup.getUnitById(atkId);
			if(atk != null) {
				//打出去的伤害
				atk.getStatisticsEngine().addAtkHurt(hurt);
			}
		}
		getStatisticsEngine().addBearHurt(hurt);//收到的伤害
	}
}
