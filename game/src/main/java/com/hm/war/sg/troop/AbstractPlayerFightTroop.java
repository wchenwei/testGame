package com.hm.war.sg.troop;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.commander.biz.CommanderBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.tank.biz.TankFettersBiz;
import com.hm.action.tank.biz.TankSpecialBiz;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.templaextra.FettersImpl;
import com.hm.db.PlayerUtils;
import com.hm.enums.TankRareType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.coll.CollectionUtil;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.redis.tank.PlayerTankBind;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.guild.GuildItemContainer;
import com.hm.war.sg.*;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitExtraType;
import com.hm.war.sg.unit.UnitSetting;
import com.hm.util.RandomUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 抽象玩家战斗部队
 * @author siyunlong  
 * @date 2019年1月24日 上午11:38:54 
 * @version V1.0
 */
public abstract class AbstractPlayerFightTroop extends AbstractFightTroop{
	private long playerId;
	private long advanceSkillTime;//突进技能生效截止时间
	
	public AbstractPlayerFightTroop(long playerId,String troopId) {
		this.id = troopId;
		this.playerId = playerId;
	}

	public long getPlayerId() {
		return playerId;
	}
	
	@Override
	public FightTroopType getFightTroopType() {
		return FightTroopType.Player;
	}
	@Override
	public UnitGroup createUnitGroup() {
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		TankSkillConfig tankSkillConfig = SpringUtil.getBean(TankSkillConfig.class);
		CommanderBiz commanderBiz = SpringUtil.getBean(CommanderBiz.class);
		GuildTechBiz guildTechBiz = SpringUtil.getBean(GuildTechBiz.class);
		TankFightBiz tankFightBiz = SpringUtil.getBean(TankFightBiz.class);
		TankFettersBiz tankFettersBiz = SpringUtil.getBean(TankFettersBiz.class);
		TankSpecialBiz tankSpecialBiz = SpringUtil.getBean(TankSpecialBiz.class);
		//羁绊数据
		List<FettersImpl> fettersSkills = tankFettersBiz.getFettersSkill(this.tankList);
		
		UnitGroup unitGroup = new UnitGroup();
		Player player = getPlayer();
		Guild guild = getPlayerGuild(player);
		
		unitGroup.setPlayerId(playerId);
		unitGroup.loadPlayerInfo(player, guild);
		unitGroup.setTroopId(getId());
		unitGroup.setCombat(getSumCombat(player));
		unitGroup.setMoraleMax(moraleMax);
		unitGroup.setSuperWeaponLv(player.playerCommander().getSuperWeaponLv());
		unitGroup.setTroopBuffList(Lists.newArrayList(getTroopBuffList()));
        unitGroup.setGroupAircraft(tankFightBiz.buildGroupAircraft(player, this));
		Map<Integer,Double> attrRateMap = buildExtraAttrRate(player, guild);
		Map<Integer,Integer> guildTechSkillLvs = Maps.newHashMap();
		//每个坦克的额外百分比属性
		Map<Integer,Map<Integer,Double>> tankExtraAttrMap = tankFightBiz.calTankExtraAttr(player, fettersSkills);
		//战斗额外属性加成
		Map<Integer,Double> allAttrAddMap = buildAllTankAddAttr(player);
		//随机一个坦克增加的技能列表
		List<Skill> randomOneTankSkillList = tankFightBiz.randomOneTankSkillList(player);
		if(guild != null) {
			unitGroup.setFlag(guild.getGuildFlag().getFlagName());
			//部落科技增加技能
			guildTechSkillLvs = getGuildTechSkill(guild, player);
			CollectionUtil.mapAddMap(attrRateMap, guildTechBiz.getFightAttrRate(player, this.fightType));
			
			int artillerySupportSkillId = getGuildSupportSkillId(player, this.cityId);
			if(artillerySupportSkillId > 0) {
				randomOneTankSkillList.add(new Skill(1, tankSkillConfig.getSkillSetting(artillerySupportSkillId)));
			}
		}
		//判断破城额外增加技能
		this.extraSkillLvs.putAll(tankFightBiz.calPlayerFightSkill(player, this));
		this.extraSkillLvs.putAll(buildPlayerExtraSkill(player));
		this.extraSkillLvs.putAll(buildTroopBuffSkillMap());
		//加载坦克的额外技能
		ArrayListMultimap<Integer,Integer> tankExtraSkillMap = loadTankExtraSkill();
		Set<Integer> commanderSkillList = commanderBiz.getCarWarSkillList(player);

		for (TankArmy tankArmy : buildTankList(player, unitGroup)) {
			if(tankArmy.isDeath()) {
				continue;
			}
			int index = tankArmy.getIndex();
			int tankId = tankArmy.getId();
			Tank tank = player.playerTank().getTank(tankId);
			if(tank != null) {
				TankSetting tankConfSetting = tankConfig.getTankSetting(tankId);
				UnitSetting unitSetting = new UnitSetting(tank,tankConfSetting);
				Unit unit = new Unit(unitSetting);
				unit.setIndex(index);
				//添加额外技能
				unit.getSetting().addExtraSkillId(this.extraSkillLvs);
				//重新计算属性加成
				//========================属性百分比加成====================================
				Map<Integer,Double> tankAttrRateMap = Maps.newHashMap(attrRateMap);
				CollectionUtil.mapAddMap(tankAttrRateMap, tankExtraAttrMap.get(tankId));
				unitSetting.getUnitAttr().calAttrRate(tankAttrRateMap);
				//========================增加固定属性值====================================
				unitSetting.getUnitAttr().addAttrMap(allAttrAddMap);
				//设置技能s
				Map<Integer,Integer> tankSkillMap = tankFightBiz.recalTankSkill(player,fettersSkills, unit);
				unit.getUnitSkills().buildSkill(tankSkillConfig,tankSkillMap);
				//添加坦克额外技能
				unit.getUnitSkills().addSkill(tankFightBiz.calTankExtraList(player, tank));
				//添加坐骑额外技能
				List<Skill> commanderSkills = commanderSkillList.stream()
						.map(skillId -> new Skill(1,tankSkillConfig.getSkillSetting(skillId))).collect(Collectors.toList());
				unit.getUnitSkills().addSkill(commanderSkills);
				//添加部落科技技能
				List<Skill> guildSkills = guildTechSkillLvs.entrySet().stream()
						.filter(e -> tankSkillConfig.getSkillSetting(e.getKey()) != null)
						.map(e -> new Skill(e.getValue(),tankSkillConfig.getSkillSetting(e.getKey()))).collect(Collectors.toList());
				unit.getUnitSkills().addSkill(guildSkills);
				//增加坦克钻技能
				List<Skill> specialSkills = tankSpecialBiz.getSpecialSkill(tank).stream()
						.map(e -> new Skill(1,tankSkillConfig.getSkillSetting(e))).collect(Collectors.toList());
				unit.getUnitSkills().addSkill(specialSkills);
				//加载坦克的额外技能
				List<Skill> extraSkills = tankExtraSkillMap.get(tankId).parallelStream()
						.map(e -> new Skill(1,tankSkillConfig.getSkillSetting(e))).collect(Collectors.toList());
				unit.getUnitSkills().addSkill(extraSkills);
				//检查突破技能是否删除原有坦克技能
				tankFightBiz.checkRepaceTankSkill(unit, tankConfSetting);
				//同步保留状态
				loadRetainState(unit, tankArmy);
				unitGroup.addUnit(unit);
			}
		}
		GroupHorse.loadHorse(player,unitGroup);//加载坐骑

		//随机给一个坦克加技能
		if(CollUtil.isNotEmpty(randomOneTankSkillList)) {
			List<Unit> lifeUnit = unitGroup.getLifeUnit();
			if(CollUtil.isNotEmpty(lifeUnit)) {
				RandomUtils.randomEle(lifeUnit).getUnitSkills().addSkill(randomOneTankSkillList);
			}
		}
		doSetStrangeTankInfo(unitGroup, player);//计算奇兵坦克特殊展示
		return unitGroup;
	}
	/**
	 * 计算奇兵特殊类型
	 * @param unitGroup
	 * @param player
	 */
	public void doSetStrangeTankInfo(UnitGroup unitGroup,Player player) {
		List<Integer> troopTankIds = this.tankList.stream().map(e -> e.getId()).collect(Collectors.toList());
		for (Unit unit : unitGroup.getUnits()) {
			int tankId = unit.getSetting().getTankId();
			if(!troopTankIds.contains(tankId)) {
				//奇兵
				unit.getSetting().addUnitExtraType(UnitExtraType.StrangeTank);
				//找出绑定他的sss坦克
				Unit sssTank = unitGroup.getUnits().stream().filter(e -> {
					Tank tank = player.playerTank().getTank(e.getSetting().getTankId());
					return tank != null && tank.getSSSStrangeTankId() == tankId;
				}).findFirst().orElse(null);
				if(sssTank != null) {
					sssTank.getSetting().addUnitExtraType(UnitExtraType.CallSSSStrangeTank);
				}
				return;
			}
		}
	}
	
	
	public List<TankArmy> buildTankList(Player player,UnitGroup unitGroup) {
		if(this.tankList.size() >= 9) {
			return this.tankList;
		}
		//当前上阵的坦克id列表
		List<Integer> tankIdsList = this.tankList.stream()
				.sorted(Comparator.comparingInt(TankArmy::getIndex))
				.map(e -> e.getId()).collect(Collectors.toList());
		//判断是否包含sss坦克奇兵
		List<Tank> luckList = this.tankList.stream()
		.filter(e -> !e.isDeath())	
		.map(e -> player.playerTank().getTank(e.getId()))
                .filter(Objects::nonNull)
		.filter(e -> e.getSSSStrangeTankId() > 0)
		.filter(e -> !tankIdsList.contains(e.getSSSStrangeTankId()))//现在的部队里不包含奇兵
		.collect(Collectors.toList());
		if(CollUtil.isEmpty(luckList)) {
			return tankList;
		}
		//奇兵列表 key:sss坦克ID  val:奇兵id
		Map<Integer,Integer> strangeTankMap = luckList.stream()
				.collect(Collectors.toMap(Tank::getId, Tank::getSSSStrangeTankId));
		if(CollUtil.isEmpty(strangeTankMap)) {
			return tankList;
		}
		//随机一个奇兵
		TroopStrangeTankMode troopStrangeTankMode = new TroopStrangeTankMode(player, strangeTankMap, tankIdsList);
		unitGroup.setTroopStrangeTankMode(troopStrangeTankMode);
		int tankId = troopStrangeTankMode.getLuckTankId();
		if(tankId > 0) {//随机到了 奇兵,找位置
			int luckIndex = findLuckIndex(tankList, player.playerTank().getTank(tankId));
			List<TankArmy> newList = Lists.newArrayList(this.tankList);
			newList.add(new TankArmy(luckIndex, tankId));
			return newList;
		}
		return tankList;
	}
	
	public List<TankArmy> buildKFTankList(Player player,UnitGroup unitGroup) {
		if(this.tankList.size() >= 9) {
			return this.tankList;
		}
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		boolean haveSSSTank = this.tankList.stream()
				.filter(e -> !e.isDeath())	
				.anyMatch(e -> tankConfig.getTankSetting(e.getId()).getRare() >= TankRareType.SSSR.getType());
		if(!haveSSSTank) {
			return this.tankList;
		}
		final PlayerTankBind playerTankBind = PlayerTankBind.getPlayerTankBind(player.getId());
		if(playerTankBind == null) {
			return this.tankList;
		}
		//当前上阵的坦克id列表
		List<Integer> tankIdsList = this.tankList.stream()
				.sorted(Comparator.comparingInt(TankArmy::getIndex))
				.map(e -> e.getId()).collect(Collectors.toList());
		//判断是否包含sss坦克奇兵
		Map<Integer,Integer> strangeTankMap = this.tankList.stream()
		.filter(e -> !e.isDeath())	
		.map(e -> e.getId())
		.filter(e -> playerTankBind.getBindTankId(e) > 0)
		.filter(e -> !tankIdsList.contains(playerTankBind.getBindTankId(e)))//现在的部队里不包含奇兵
		.collect(Collectors.toMap(e -> e, e -> {
			return playerTankBind.getBindTankId(e);
		}));
		if(CollUtil.isEmpty(strangeTankMap)) {
			return tankList;
		}
		//随机一个奇兵
		TroopStrangeTankMode troopStrangeTankMode = new TroopStrangeTankMode(player, strangeTankMap, tankIdsList);
		unitGroup.setTroopStrangeTankMode(troopStrangeTankMode);
		int tankId = troopStrangeTankMode.getLuckTankId();
		if(tankId > 0) {//随机到了 奇兵,找位置
			int luckIndex = findLuckIndex(tankList, player.playerTank().getTank(tankId));
			List<TankArmy> newList = Lists.newArrayList(this.tankList);
			newList.add(new TankArmy(luckIndex, tankId));
			return newList;
		}
		return tankList;
	}
	
	public int findLuckIndex(List<TankArmy> tankList,Tank tank) {
		TankBiz tankBiz = SpringUtil.getBean(TankBiz.class);
		Map<Integer,Integer> tankIndexMap = tankList.stream().collect(Collectors.toMap(TankArmy::getIndex, TankArmy::getId));
		tankBiz.putTankData(tank, tankIndexMap);
		for (Map.Entry<Integer,Integer> entry : tankIndexMap.entrySet()) {
			if(entry.getValue() == tank.getId()) {
				return entry.getKey();
			}
		}
		//没有找到位置,随机一个位置
		List<Integer> indexList = Lists.newArrayList();
		for (int i = 0; i < 9; i++) {
			indexList.add(i);
		}
		for (TankArmy tankArmy : tankList) {
			indexList.remove(Integer.valueOf(tankArmy.getIndex()));
		}
		return RandomUtils.randomEle(indexList);
	}
	
	
	/**
	 * 获取部落科技炮火支援技能
	 * @param guild
	 * @param cityId
	 * @return
	 */
	public int getGuildSupportSkillId(Player player,int cityId) {
		GuildTechBiz guildTechBiz = SpringUtil.getBean(GuildTechBiz.class);
		return guildTechBiz.getArtillerySupportSkillId(player, cityId);
	}
	
	public Map<Integer,Integer> getGuildTechSkill(Guild guild,Player player) {
		GuildTechBiz guildTechBiz = SpringUtil.getBean(GuildTechBiz.class);
		return guildTechBiz.getGuildTechSkills(guild,player,this.cityId,this.fightType);
	}
	
	/**
	 * 计算玩家额外的战斗属性加成 要新建map！！
	 * @param player
	 * @param guild
	 * @return
	 */
	public Map<Integer,Double> buildExtraAttrRate(Player player,Guild guild) {
		return Maps.newHashMap();
	}
	public Map<Integer,Integer> buildPlayerExtraSkill(Player player) {
		return Maps.newHashMap();
	}
	
	//计算所有战斗单元属性额外添加
	public Map<Integer,Double> buildAllTankAddAttr(Player player) {
		TankFightBiz tankFightBiz = SpringUtil.getBean(TankFightBiz.class);
		return tankFightBiz.calExtraAttrMap(player, this);
	}
	
	public Player getPlayer() {
		return PlayerUtils.getPlayer(playerId);
	}

	public Guild getPlayerGuild(Player player) {
		GuildItemContainer guildItemContainer = GuildContainer.of(player);
		if(guildItemContainer == null) {
			return null;
		}
		return guildItemContainer.getGuild(player.getGuildId());
	}
	
	@Override
	public void doWarResult(UnitGroup unitGroup) {
		super.doWarResult(unitGroup);
		unitGroup.getUnits().forEach(e -> {
			TankArmy tankArmy = getTankArmy(e.getSetting().getTankId());
			if(tankArmy == null) {
				return;
			}
			if(e.isDeath()) {
				tankArmy.setHp(0);
				return;
			}
			tankArmy.setUnitRetainType(UnitRetainType.HP, e.getHp());
			//注释蓝量继承
//			tankArmy.setUnitRetainType(UnitRetainType.MP, (long)e.getMpEngine().getMp());
		});
	}
	
	public TankArmy getTankArmy(int id) {
		return tankList.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
	
	public long getSumCombat(Player player) {
		return tankList.stream().map(e -> player.playerTank().getTank(e.getId()))
				.filter(e -> e != null)
				.mapToLong(e -> e.getCombat()).sum();
	}

	public long getAdvanceSkillTime() {
		return advanceSkillTime;
	}

	public void setAdvanceSkillTime(long advanceSkillTime) {
		this.advanceSkillTime = advanceSkillTime;
	}

	@Override
	public int getGuildId() {
		Player player = PlayerUtils.getPlayer(this.playerId);
		return player != null?player.getGuildId():0;
	}

	public long getTroopCombat(Player player) {
		return getTankList().stream().map(e -> player.playerTank().getTank(e.getId()))
				.filter(Objects::nonNull).mapToLong(e -> e.getCombat()).sum();
	}

}
