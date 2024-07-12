package com.hm.war.sg.troop;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.TankSkillConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.FightType;
import com.hm.enums.MoraleReduceType;
import com.hm.war.sg.*;
import com.hm.war.sg.unit.Unit;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public abstract class AbstractFightTroop {
	protected String id;
	private boolean isAtk;
	protected boolean isSameGuild;//是否和城池在一个部落
	protected boolean isNpc;
	protected int moraleMax;//士气最大值
	//出站的坦克列表
	protected List<TankArmy> tankList = Lists.newArrayList();
	private int formationId;//航母部队id
	//战斗类型
	protected FightType fightType;
	protected int cityId;//所在城市
	protected MoraleReduceType moraleReduceType = MoraleReduceType.AllYes;
	protected Map<Integer,Integer> extraSkillLvs = Maps.newHashMap();
	protected Set<Integer> troopBuffList = Sets.newHashSet();
	
	public abstract UnitGroup createUnitGroup();
	public abstract FightTroopType getFightTroopType();
	public abstract int getGuildId();

	//加载坦克的额外技能
	public ArrayListMultimap<Integer,Integer> loadTankExtraSkill() {
		return ArrayListMultimap.create();
	}	
	
	public void clearTroopFightInfo() {
		this.extraSkillLvs.clear();
		this.troopBuffList.clear();
	}


	public void doWarResult(UnitGroup unitGroup) {
		//每局结束最大士气减少
		int minValue = SettingManager.getInstance().getCommValue(CommonValueType.MoraleMin);
		int reduceValue = SettingManager.getInstance().getCommValue(CommonValueType.MoraleReduceFight);
		this.moraleMax = Math.max(unitGroup.getMoraleMax()-reduceValue, minValue);
	}
	public AbstractFightTroop() {
		this.moraleMax = SettingManager.getInstance().getCommValue(CommonValueType.MoraleMax);
	}
	public void loadFightData(boolean isAtk) {
		this.isAtk = isAtk;
	}
	
	public void loadFightParm(FightType fightType,int cityId) {
		this.fightType = fightType;
		this.cityId = cityId;
	}
	
	public void loadRetainState(Unit unit, TankArmy tankArmy) {
		if(tankArmy.isFullHp()) {
			unit.getSetting().setFullHp(true);
		}else{
			if(tankArmy.hasUnitRetainType(UnitRetainType.HP)) {//血量
				unit.getSetting().setInitHp(tankArmy.getUnitRetain(UnitRetainType.HP));
			}else {
				unit.getSetting().setInitHp(tankArmy.getHp());
			}
			unit.getHpEngine().setInitHp(unit.getSetting().getInitHp());
		}
//		if(tankArmy.hasUnitRetainType(UnitRetainType.MP)) {//蓝量
//			unit.getMpEngine().addMp(tankArmy.getUnitRetain(UnitRetainType.MP));
//		}
	}
	
	//清空保留状态
	public void clearRetainState() {
		this.tankList.forEach(e -> e.clearUnitRetain());
	}
	
	public long getTotalHp() {
		return tankList.stream().mapToLong(e -> e.getHp()).sum();
	}
	
	public boolean haveLifeTank() {
		return this.tankList.stream().anyMatch(e -> e.getHp() > 0 || e.isFullHp());
	}
	
	public void addTroopBuff(TroopBufferType buffType) {
		this.troopBuffList.add(buffType.getType());
	}
	/**
	 * 根据部队buff构建技能列表
	 * @return
	 */
	public Map<Integer,Integer> buildTroopBuffSkillMap() {
		if(CollUtil.isEmpty(this.troopBuffList)) {
			return Maps.newHashMap();
		}
		TankSkillConfig tankSkillConfig = SpringUtil.getBean(TankSkillConfig.class);
		return tankSkillConfig.buildTroopBuffSkillList(this.troopBuffList);
	}
	
	public boolean isClonePlayer() {
		return false;
	}
}
