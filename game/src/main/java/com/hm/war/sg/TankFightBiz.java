package com.hm.war.sg;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.citywarskill.biz.CityWarSkillBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.guild.biz.GuildFactoryBiz;
import com.hm.action.memorialHall.biz.MemorialHallAttrBiz;
import com.hm.action.passenger.biz.PassengerBiz;
import com.hm.action.player.PlayerWarCraftBiz;
import com.hm.action.tank.biz.ControlBiz;
import com.hm.action.tank.biz.MagicReformBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.tank.biz.TankTechBiz;
import com.hm.config.excel.AircraftCarrierConfig;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.FightConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.CityWarSkillEnum;
import com.hm.enums.CommonValueType;
import com.hm.enums.FightType;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.coll.CollectionUtil;
import com.hm.model.player.Aircraft;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.redis.Formation;
import com.hm.redis.PlayerAirFormation;
import com.hm.util.MathUtils;
import com.hm.war.sg.aircraft.GroupAircraft;
import com.hm.war.sg.aircraft.PlaneSkill;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.troop.AbstractPlayerFightTroop;
import com.hm.war.sg.unit.Unit;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Biz
public class TankFightBiz {
	@Resource
	private WorldCityBiz worldCityBiz;
	@Resource
	private TankBiz tankBiz;
	@Resource
	private TankTechBiz tankTechBiz;
	@Resource
	private PassengerBiz passengerBiz;
	@Resource
	private TankSkillConfig tankSkillConfig;
	@Resource
	private MagicReformBiz magicReformBiz;
	@Resource
	private MemorialHallAttrBiz memorialHallAttrBiz;
	@Resource
	private PlayerWarCraftBiz playerWarCraftBiz;
	@Resource
	private GuildFactoryBiz guildFactoryBiz;
	@Resource
	private CityWarSkillBiz cityWarSkillBiz;
	@Resource
	private FightConfig fightConfig;
	@Resource
	private AircraftCarrierConfig aircraftCarrierConfig;
    @Resource
    private ControlBiz controlBiz;
	@Resource
	private CommanderConfig commanderConfig;

	/**
	 * 计算战斗额外属性
	 * @return
	 */
	public Map<Integer,Double> calExtraAttrMap(Player player, AbstractPlayerFightTroop fightTroop) {
		Map<Integer,Double> extraAttrMap = Maps.newHashMap();
		//城战额外属性
		CollectionUtil.mapAddMap(extraAttrMap, worldCityBiz.calWorldCityAttr(player, fightTroop.getCityId()));
		return extraAttrMap;
	}
	
	/**
	 * 计算单坦克额外技能列表
	 * @param player
	 * @param tank
	 * @return
	 */
	public List<Skill> calTankExtraList(Player player, Tank tank) {
		List<Skill> skillList = Lists.newArrayList();
		Set<Integer> skillIds = Sets.newHashSet();
		
		//============================技能列表==========================================
		//增加坦克芯片技能
		skillList.addAll(tankTechBiz.getTankStarSkill(tank));
        //增加坦克中控技能
        skillList.addAll(controlBiz.getControlSkill(tank));
		//=======================技能id列表,默认都是1级技能========================================
		//套装技能
		skillIds.addAll(passengerBiz.getTankPassengerSkillIds(player, tank));
		//武器技能
		skillIds.addAll(guildFactoryBiz.getArmsSkillIds(player,tank));

		//===============================================================
		skillIds.stream().map(e -> tankSkillConfig.getSkillSetting(e))
		.filter(Objects::nonNull).map(e -> new Skill(1, e))
		.forEach(e -> skillList.add(e));


        return skillList;
	}
	
	/**
	 * 随机给我方一辆坦克增加技能
	 * @param player
	 * @return
	 */
	public List<Skill> randomOneTankSkillList(Player player) {
		List<Skill> skillList = Lists.newArrayList();
		//增加兵法技能
		skillList.addAll(playerWarCraftBiz.getWarCraftSkillList(player));
		return skillList;
	}
	
	/**
	 * 加载战斗组属性
	 * @param atkGroup
	 * @param defGroup
	 */
	public void loadFightUnitGroup(UnitGroup atkGroup,UnitGroup defGroup) {
		loadFightForMilitary(atkGroup, defGroup);//计算军衔压制
		loadCombatDValueForAttr(atkGroup, defGroup);//计算战力压制
	}
	
	/**
	 * 加载军衔压制
	 * @param atkGroup
	 * @param defGroup
	 */
	public void loadFightForMilitary(UnitGroup atkGroup, UnitGroup defGroup) {
		int atkLv = atkGroup.getGroupHorse() != null ? atkGroup.getGroupHorse().getMilitaryLv():0;
		int defLv = defGroup.getGroupHorse() != null ? defGroup.getGroupHorse().getMilitaryLv():0;
		if(atkLv == defLv) {
			return;
		}
		//额外伤害加成值
		UnitGroup luckGroup = atkLv > defLv?atkGroup:defGroup;
		int lv = atkLv > defLv?atkLv:defLv;
		MilitaryExtraTemplate template = commanderConfig.getMilitaryExtraTemplate(lv);
		double addValue = template.getStifleVal();//获取压制数值
		for (Unit unit : luckGroup.getLifeUnit()) {
			unit.getSetting().getUnitAttr().addAttr(TankAttrType.FinalAddAtkPer, addValue);
		}
	}
	
	/**
	 * 战斗部队战力压制
	 * @param atkGroup
	 * @param defGroup
	 */
	public void loadCombatDValueForAttr(UnitGroup atkGroup,UnitGroup defGroup) {
		try {
			FightTroopType atkTroopType = atkGroup.getFightTroopType();
			FightTroopType defTroopType = defGroup.getFightTroopType();
			if(atkTroopType == FightTroopType.Player && defTroopType == FightTroopType.Player) {
				long atkCombat = atkGroup.getCombat();
				long defCombat = defGroup.getCombat();
				long highCombat = Math.max(atkCombat, defCombat);
				long lowCombat = Math.min(atkCombat, defCombat);
				double oneValue = SettingManager.getInstance().getDoubleCommValue(CommonValueType.FightCombatSuppress);
				double addValue = Math.log(MathUtils.div(Math.max(0, highCombat-1000000), 10000)+1)*(MathUtils.div(highCombat-lowCombat, highCombat))*oneValue;
				if(addValue > 0) {
					UnitGroup luckGroup = atkCombat > defCombat?atkGroup:defGroup;
					for (Unit unit : luckGroup.getLifeUnit()) {
						unit.getSetting().getUnitAttr().addAttr(TankAttrType.ReduceAtkPer, addValue);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重新计算坦克技能，用于处理替换坦克技能的处理
	 * @param fettersSkills
	 * @param unit
	 * @return
	 */
	public Map<Integer,Integer> recalTankSkill(Player player,List<FettersImpl> fettersSkills,Unit unit) {
		//羁绊替换技能之后
		Map<Integer,Integer> skillMap = calTankFetterSkill(fettersSkills, unit);
		magicReformBiz.calTankSkill(player,unit.getSetting().getTankId(),skillMap);
		return skillMap;
	}
	
	public Map<Integer,Integer> recalTankSkill(Tank tank,List<FettersImpl> fettersSkills,Unit unit) {
		//羁绊替换技能之后
		Map<Integer,Integer> skillMap = calTankFetterSkill(fettersSkills, unit);
		magicReformBiz.calTankSkill(tank,skillMap);
		return skillMap;
	}
	
	/**
	 * 计算坦克突破技能替换
	 * @param unit
	 * @param tankConfSetting
	 */
	public void checkRepaceTankSkill(Unit unit,TankSetting tankConfSetting) {
		Map<Integer,Integer> repaceSkillMap = tankConfSetting.getRepaceSkillMap();
		if(repaceSkillMap.isEmpty()) {
			return;
		}
		for (Map.Entry<Integer,Integer> entry : repaceSkillMap.entrySet()) {
			int skillId = entry.getKey();
			int delId = entry.getValue();
			SkillSetting skillSetting = tankSkillConfig.getSkillSetting(skillId);
			if(skillSetting != null) {
				unit.getUnitSkills().checkRepaceSkillAndDel(skillSetting, delId);
			}
		}
	}
	
	/**
	 * 获取新羁绊的替换技能
	 * @param unit
	 * @return
	 */
	public Map<Integer,Integer> calTankFetterSkill(List<FettersImpl> fettersSkills,Unit unit) {
		final Map<Integer,Integer> skillMap = unit.getSetting().getSkillMap();
		if(CollUtil.isEmpty(fettersSkills)) {
			return skillMap;
		}
		FettersImpl fettersSkill = fettersSkills.stream()
		.filter(e -> e.getNew_skill() > 0)
		.filter(e -> e.getBelong() == unit.getSetting().getId())
		.filter(e -> skillMap.containsKey(e.getOld_skill())).findFirst().orElse(null);
		if(fettersSkill != null) {
			Map<Integer,Integer> newMap = Maps.newHashMap(skillMap);
			int oldSkillLv = newMap.remove(fettersSkill.getOld_skill());
			newMap.put(fettersSkill.getNew_skill(), oldSkillLv);
			return newMap;
		}
		return skillMap;
	}
	
	/**
	 * 根据羁绊计算每个坦克额外增加属性 %属性
	 * @param player
	 * @param fettersSkills
	 * @return
	 */
	public Map<Integer,Map<Integer,Double>> calTankExtraAttr(Player player,List<FettersImpl> fettersSkills) {
//		return memorialHallAttrBiz.calFetterBuffAttr(player, fettersSkills);
		return Maps.newHashMap();
	}

	public Map<Integer, Integer> buildNpcTankSkillMap(PvpNpcTemplate pvpNpcTemplate){
		Map<Integer, Integer> skillMap = Maps.newHashMap();
		// 星级技能
		skillMap.putAll(tankTechBiz.getTankStarSkillMap(pvpNpcTemplate.getModel(), pvpNpcTemplate.getStar()));
		return skillMap;
	}

	/**
	 * 计算玩家格外战斗熟悉
	 * @param player
	 * @param troop
	 * @return
	 */
	public Map<Integer,Integer> calPlayerFightSkill(Player player,AbstractPlayerFightTroop troop) {
		Map<Integer,Integer> extraSkillLvs = Maps.newHashMap();
		int cityId = troop.getCityId();
		if(cityId <= 0) {
			return extraSkillLvs;
		}
		if(troop.getFightType() == FightType.Pvp && troop.isAtk()) {
			//是攻击方的偷袭,添加偷袭的额外技能
			extraSkillLvs.putAll(cityWarSkillBiz.getSkillAndLv(player, CityWarSkillEnum.PvpOneByOneLaunch));
		}
		long advanceSkillTime = troop.getAdvanceSkillTime();
		if(System.currentTimeMillis() < advanceSkillTime) {
			//是额外的突进技能
			extraSkillLvs.putAll(cityWarSkillBiz.getSkillAndLv(player, CityWarSkillEnum.TroopAdvance));
		}
		return extraSkillLvs;
	}
	
	/**
	 * 构建战斗航母
	 * @param player
	 * @param airId
	 * @return
	 */
	public GroupAircraft buildGroupAircraft(Player player, AbstractPlayerFightTroop fightTroop) {
		int airId = fightTroop.getFormationId();
		if(airId <= 0) {
			return null;
		}
		Formation formation = PlayerAirFormation.getOrCreate(player.getId()).getFormation(airId);
		if(formation == null) {
			return null;
		}
		//所有坦克都添加的技能
		GroupAircraft groupAircraft = new GroupAircraft();
		for (String uid : formation.getAirs()) {
			Aircraft aircraft = player.playerAircraft().getAircraft(uid);
			if(aircraft!=null){
				ItemBattleplaneTemplateImpl template = aircraftCarrierConfig.getAirTemplate(aircraft.getId());
				if(template!=null) {
					int skillId = template.getSkill_id();
					int skillLv = Math.max(1, aircraft.getStar());
                    PlaneSkill planeSkill = new PlaneSkill(aircraft, template);
                    planeSkill.setSkill(new Skill(skillLv, tankSkillConfig.getSkillSetting(skillId)));
                    groupAircraft.addPlane(planeSkill);
                }
            }
        }
        //每个坦克都增加的技能map
        Map<Integer, Integer> extraSkillLvs = fightTroop.getExtraSkillLvs();
        //添加给每个坦克添加岛屿技能
        if (groupAircraft.havePlaneSkill() && CollUtil.isNotEmpty(player.playerAircraftCarrier().getIsland())) {
            for (CvIslandTemplateImpl cvIslandTemplate : aircraftCarrierConfig.getPlayerIsland(player)) {
                if (cvIslandTemplate.getSkill_id() > 0) {
                    extraSkillLvs.put(cvIslandTemplate.getSkill_id(), cvIslandTemplate.getLevel());
				}
			}
		}
		return groupAircraft;
	}
	
	
	
	
	
	
	
	
	
	
	
}
