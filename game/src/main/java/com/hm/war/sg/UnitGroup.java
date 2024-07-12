package com.hm.war.sg;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.enums.CommonValueType;
import com.hm.enums.TankAttrType;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import com.hm.redis.util.RedisUtil;
import com.hm.war.sg.aircraft.GroupAircraft;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.DeathEvent;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.event.HurtEvent;
import com.hm.war.sg.skillnew.SkillType;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.TroopStrangeTankMode;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitExtraType;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class UnitGroup extends SimplePlayerVo{
	private int id;
	private int superWeaponLv;//超武等级
	private String troopId;
	private int serverId;
	
	private List<Unit> units = Lists.newArrayList();
	private GroupHorse groupHorse;//主公坐骑
	private GroupAircraft groupAircraft;//主公航母
	private int morale = 100;//当前士气值
	private int moraleMax;//士气最大值
	private double reduceAtkDefRate;//衰减比例
	private List<Items> itemList = Lists.newArrayList();
	private List<Items> playerDropList;//玩家掉落列表
	private int captiveTankId = -1;//俘虏的tankid
	private TroopStrangeTankMode troopStrangeTankMode;
	protected List<Integer> troopBuffList;//部队buff展示
	private int fightType;//战斗单元类型

	@Transient
	private transient WarParam warParam;//测试模式下的event记录
	@Transient
	private transient long lastMoraleFrame = 10;
	@Transient
	private transient AbstractFightTroop fightTroop;//部队实体
	@Transient
	private transient Frame curFrame;
	@Transient
	private transient boolean isAtk;
	@Transient
	private transient boolean isNoCMorale;//是否不掉士气 =true不掉士气

	public UnitGroup() {
	}
	
	public void loadId(boolean isAtk) {
		this.isAtk = isAtk;
		this.id = isAtk ? -1:-2;
		units.stream().forEach(unit -> unit.setId(isAtk?unit.getIndex():unit.getIndex()+10));
		this.lastMoraleFrame = SettingManager.getInstance().getCommValue(CommonValueType.MoraleNotReduceSecond);
	}
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}
	
	public Unit getUnitById(int id) {
		return units.stream().filter(Objects::nonNull)
				.filter(e -> !e.isDeath() && e.getId() == id).findFirst().orElse(null);
	}
	
	public void loadDefGroup(UnitGroup defGroup) {
		this.units.forEach(e -> e.loadGroup(this, defGroup));
	}
	
	public boolean isAllDeath() {
		return units.stream().filter(Objects::nonNull)
				.filter(e -> !e.isDeath()).findFirst().orElse(null) == null;
	}
	
	public List<Unit> getLifeUnit() {
		return units.stream().filter(Objects::nonNull)
				.filter(e -> !e.isDeath()).collect(Collectors.toList());
	}

	/**
	 * 释放指定类型的技能
	 */
	public void doReleaseSkillForType(Frame frame, SkillType skillType) {
		getLifeUnit().forEach(e -> e.getUnitSkills().doReleaseSkillForType(frame, skillType));
	}

    //随机一个坦克释放技能
    public void doRandomUnitReleaseSkillForType(Frame frame, SkillType skillType) {
        List<Unit> unitList = getLifeUnit();
        if (CollUtil.isNotEmpty(unitList)) {
            unitList.get(0).getUnitSkills().doReleaseSkillForType(frame, skillType);
        }
    }
	
	/**
	 * 获取我/队友是否有可触发的死亡技能
	 * @param deathUnit
	 * @return
	 */
	public boolean doReviveSkill(Frame frame,Unit deathUnit) {
		//死者有不能复活buf
		if(deathUnit.getUnitBuffs().haveBuff(UnitBufferType.NotReviveBuff)) {
			return false;
		}
		if(deathUnit.getUnitSkills().triggerReviveSkill(frame,deathUnit)) {
			return true;
		}
		for (Unit unit : getLifeUnit()) {
			if(unit.getUnitSkills().triggerReviveSkill(frame,deathUnit)) {
				return true;
			}
		}
		return false;
	}
	
	//计算成员的最大血量
	public void doCalMaxHp() {
		getLifeUnit().forEach(unit -> {
			//计算最大血量
			unit.calMaxHp();
		});
	}
	
	public Map<Integer,Unit> getLifeUnitMap() {
		return units.stream().filter(Objects::nonNull)
				.filter(e -> !e.isDeath()).collect(Collectors.toMap(Unit::getIndex, Function.identity()));
	}
	
	//获取总杀敌数量
	public int getKillTankNum() {
		return units.stream().mapToInt(e -> e.getStatisticsEngine().getKillTankNum()).sum();
	}
	
	//获取坦克总血量
	public long getTotalTankHp() {
		return getLifeUnit().stream().mapToLong(e -> e.getHp()).sum();
	}
	
	//所有坦克死亡
	public void tankAllDeath(Frame frame) {
		getLifeUnit().forEach(unit -> {
			long hurt = unit.getHp();
			unit.getHpEngine().setInitHp(0);
			frame.addEvent(new HurtEvent(unit, hurt, 0, 0, 0, 0, 1));
		});
	}
	
	public void checkMorale(Frame frame) {
		if(!isNoCMorale && frame.getId() > lastMoraleFrame) {
			this.morale -= SettingManager.getInstance().getCommValue(CommonValueType.MoraleReduceSecond);
			this.lastMoraleFrame = frame.getId()+WarComm.MoraleInterval;
			checkMoraleTankDoge();
		}
	}
	
	public void setMoraleMax(int value) {
		int minValue = SettingManager.getInstance().getCommValue(CommonValueType.MoraleMin);
		this.moraleMax = Math.max(value, minValue);
		this.morale = this.moraleMax;
		int MaxValue = SettingManager.getInstance().getCommValue(CommonValueType.MoraleMax);
		double reduceRate = SettingManager.getInstance().getDoubleCommValue(CommonValueType.MoraleAtkDefRate);
		this.reduceAtkDefRate = 1-Math.min((MaxValue-this.moraleMax)*reduceRate, 1);
		checkMoraleTankDoge();
	}
	/**
	 * 士气 > 0 才可以放技能
	 * @return
	 */
	public boolean isCanReleaseSkill() {
		return this.morale > 0;
	}
	
	/**
	 * 士气为0时,闪避全部变成1
	 */
	public void checkMoraleTankDoge() {
		if(this.morale != 0) {
			return;
		}
		units.forEach(e -> {
			e.getSetting().getUnitAttr().putAttr(TankAttrType.DODGE, 1);
		});
	}
	
	public void checkEndUnit(Frame frame) {
		for (Unit unit : units) {
			if(!unit.isDeath() && unit.getUnitBuffs().haveBuff(UnitBufferType.NextDeathBuff)
					|| unit.getSetting().haveUnitExtraType(UnitExtraType.StrangeTank)) {
				unit.getHpEngine().setInitHp(0);
				frame.addEvent(new DeathEvent(unit.getId()));
			}
		}
	}
	public void addDropItems(List<Items> dropList) {
		this.itemList.addAll(dropList);
	}
	
	/**
	 * 获取战斗掉落
	 * @return
	 */
	public List<Items> getWarDrops() {
		List<Items> dropList = Lists.newArrayList();
		dropList.addAll(this.itemList);
		if(CollUtil.isNotEmpty(this.playerDropList)) {
			dropList.addAll(this.playerDropList);
		}
		return dropList;
	}
	
	public void loadPlayerInfo(Player player,Guild guild) {
		load(RedisUtil.getPlayerRedisData(player.getId()));
		loadGuild(guild);
		setServerId(player.getServerId());
	}
	
	//获取战斗类型
	public FightTroopType getFightTroopType() {
		if(this.fightTroop != null) {
			return fightTroop.getFightTroopType();
		}
		return null;
	}

	public void setFightTroop(AbstractFightTroop fightTroop) {
		this.fightTroop = fightTroop;
		this.fightType = fightTroop.getFightTroopType().getType();
	}

	public void addEvent(Event event) {
		if(curFrame != null) {
			curFrame.addEvent(event);
		}
	}

}

