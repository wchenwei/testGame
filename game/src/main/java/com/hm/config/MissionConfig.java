package com.hm.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.BuyChanceTemplate;
import com.hm.config.excel.temlate.MissionSpecailTemplate;
import com.hm.config.excel.temlate.YuHunBuffTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.BattleType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.battle.BaseBattle;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.weight.RandomRatio;
import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Config
public class MissionConfig extends ExcleConfig{
	private Map<Integer, MissionExtraTemplate> missionMap = Maps.newConcurrentMap(); //id,配置
	//战役配置
	private Map<Integer,MissionSpecailExtraTemplate> battleMap = Maps.newConcurrentMap();
	//战役类型配置
	private Map<Integer,MissionTypeTemplateImpl> missionTypeMap = Maps.newConcurrentMap();
	//远征配置
	private Map<Integer,ExpeditionExtraTemplate> expeditionMap = Maps.newConcurrentMap();
	//重置消耗配置
	private Map<Integer,BuyChanceTemplate> buyChanceMap = Maps.newConcurrentMap();
	//星级奖励配置
	private Map<Integer,ChallengeStarRewardTemplate> starRewardMap = Maps.newConcurrentMap();
	//历史通关奖励配置
	private Map<Integer,DutyRoadRewardExtraTemplate> dutyRoadRewardMap = Maps.newConcurrentMap();
	private int maxBuyTimes = 0; //最大购买次数
	
	private Map<Integer,MissionTreasureTemplateImpl> treasureMap = Maps.newConcurrentMap();
	//战场寻宝奖励配置
	private Map<Integer,RecoveryBattleRewardTemplate> recoveryRewardMap = Maps.newConcurrentMap();
	//战场寻宝奖励权重构建
	private Map<Integer,RandomRatio> recoveryRewardWeightMap = Maps.newConcurrentMap();
	
	private int treaureMaxLv = 0;
	
	public static int MainMissionMaxId = 0;
	
	private Map<Integer,FrontBattleTemplate> frontBattleMap = Maps.newConcurrentMap();
	
	private Table<Integer,Integer,MissionSpecailExtraTemplate> battleTable = HashBasedTable.create();
	@Getter
	private List<Integer> battleUnlockFunctions = Lists.newArrayList();
	private Map<Integer, YuHunBuffTemplate> yuHunBuffMap = Maps.newConcurrentMap();
	private WeightMeta<Integer> yuhunWeightMeta;
	

	@Override
	public void loadConfig() {
		loadMissionConfig();
		loadBattleConfig();
		loadMissionTypeConfig();
		loadExpeditionConfig();
		loadBuyChanceConfig();
		loadStarRewardConfig();
		loadHistoryRewardConfig();
		loadTreasureTemplate();
		loadRecoveryRewardConfig();
		loadFrontBattleConfig();
		loadYuHunConfig();
	}

	private void loadYuHunConfig() {
		this.yuHunBuffMap = json2ImmutableMap(YuHunBuffTemplate::getId, YuHunBuffTemplate.class);
		Map<Integer, Integer> weightMap = yuHunBuffMap.values().stream().collect(Collectors.toMap(YuHunBuffTemplate::getId, YuHunBuffTemplate::getWeight));
		this.yuhunWeightMeta = RandomUtils.buildWeightMeta(weightMap);
	}


	private void loadFrontBattleConfig() {
		Map<Integer,FrontBattleTemplate> frontBattleMap = Maps.newConcurrentMap();
		for(FrontBattleTemplate template :JSONUtil.fromJson(getJson(FrontBattleTemplate.class), new TypeReference<ArrayList<FrontBattleTemplate>>(){})){
			template.init();
			frontBattleMap.put(template.getId(), template);
		}
		this.frontBattleMap = ImmutableMap.copyOf(frontBattleMap);
	}


	private void loadRecoveryRewardConfig() {
		try {
			Map<Integer,RecoveryBattleRewardTemplate> recoveryRewardMap = Maps.newConcurrentMap();
			Table<Integer,Integer,Integer> tables =  HashBasedTable.create();
			for(RecoveryBattleRewardTemplate template :JSONUtil.fromJson(getJson(RecoveryBattleRewardTemplate.class), new TypeReference<ArrayList<RecoveryBattleRewardTemplate>>(){})){
				template.init();
				tables.put(template.getLevel(), template.getId(), template.getWeight());
				recoveryRewardMap.put(template.getId(), template);
			}
			this.recoveryRewardMap = ImmutableMap.copyOf(recoveryRewardMap);
			//构建权重
			Map<Integer,RandomRatio> recoveryRewardWeightMap = Maps.newConcurrentMap();
			for(Integer id:tables.rowKeySet()){
				recoveryRewardWeightMap.put(id, new RandomRatio(tables.row(id)));
			}
			this.recoveryRewardWeightMap = ImmutableMap.copyOf(recoveryRewardWeightMap);
			log.info("战场寻宝加载完成");
		} catch (Exception e) {
			log.info("战场寻宝加载失败");
		}
		
	}


	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MissionExtraTemplate.class,MissionSpecailExtraTemplate.class,
				MissionTypeTemplateImpl.class,ExpeditionExtraTemplate.class,
				BuyChanceTemplate.class,ChallengeStarRewardTemplate.class,
				DutyRoadRewardExtraTemplate.class,MissionTreasureTemplateImpl.class,
				RecoveryBattleRewardTemplate.class,FrontBattleTemplate.class);
	}
	private void loadMissionConfig(){
		try {
			Map<Integer, MissionExtraTemplate> missionMap = Maps.newHashMap();
			List<MissionExtraTemplate> templateList = JSONUtil.fromJson(getJson(MissionExtraTemplate.class), new TypeReference<ArrayList<MissionExtraTemplate>>(){});
			for(MissionExtraTemplate template:templateList){
				template.init();
				missionMap.put(template.getId(),template);
				if(template.getType()==1){
					this.MainMissionMaxId = Math.max(this.MainMissionMaxId, template.getId());
				}
			}
			this.missionMap = ImmutableMap.copyOf(missionMap);
			log.info("关卡加载完成");
		} catch (Exception e) {
			log.info("关卡加载失败",e);
		}
	}
	
	private void loadBattleConfig(){
		try {
			Map<Integer, MissionSpecailExtraTemplate> battleMap = Maps.newHashMap();
			Table<Integer,Integer,MissionSpecailExtraTemplate> tempBattleTable = HashBasedTable.create();
			List<MissionSpecailExtraTemplate> templateList = JSONUtil.fromJson(getJson(MissionSpecailExtraTemplate.class), new TypeReference<ArrayList<MissionSpecailExtraTemplate>>(){});
			for(MissionSpecailExtraTemplate template:templateList){
				template.init();
				battleMap.put(template.getId(),template);
				tempBattleTable.put(template.getType(), template.getId(), template);
			}
			this.battleMap = ImmutableMap.copyOf(battleMap);
			this.battleTable = ImmutableTable.copyOf(tempBattleTable);
			log.info("战役配置加载完成");
		} catch (Exception e) {
			log.info("战役配置加载失败", e);
		}
	}
	
	private void loadExpeditionConfig(){
		try {
			Map<Integer, ExpeditionExtraTemplate> battleMap = Maps.newHashMap();
			List<ExpeditionExtraTemplate> templateList = JSONUtil.fromJson(getJson(ExpeditionExtraTemplate.class), new TypeReference<ArrayList<ExpeditionExtraTemplate>>(){});
			for(ExpeditionExtraTemplate template:templateList){
				template.init();
				battleMap.put(template.getId(),template);
			}
			this.expeditionMap = ImmutableMap.copyOf(battleMap);
			log.info("远征配置加载完成");
		} catch (Exception e) {
			log.info("远征配置加载失败");
		}
	}
	
	private void loadBuyChanceConfig(){
		try {
			Map<Integer, BuyChanceTemplate> buyChanceMap = Maps.newHashMap();
			List<BuyChanceTemplate> templateList = JSONUtil.fromJson(getJson(BuyChanceTemplate.class), new TypeReference<ArrayList<BuyChanceTemplate>>(){});
			for(BuyChanceTemplate template:templateList){
				maxBuyTimes = Math.max(maxBuyTimes, template.getTimes());
				buyChanceMap.put(template.getTimes(),template);
			}
			this.buyChanceMap = ImmutableMap.copyOf(buyChanceMap);
			log.info("重置价格配置加载完成");
		} catch (Exception e) {
			log.info("重置价格配置加载失败");
		}
		
	}
	
	private void loadMissionTypeConfig(){
		try {
			List<Integer> functions = Lists.newArrayList();
			Map<Integer, MissionTypeTemplateImpl> missionTypeMap = Maps.newHashMap();
			List<MissionTypeTemplateImpl> templateList = JSONUtil.fromJson(getJson(MissionTypeTemplateImpl.class), new TypeReference<ArrayList<MissionTypeTemplateImpl>>(){});
			for(MissionTypeTemplateImpl template:templateList){
				template.init();
				functions.add(template.getUnlock_level());
				missionTypeMap.put(template.getId(),template);
			}
			this.missionTypeMap = ImmutableMap.copyOf(missionTypeMap);
			this.battleUnlockFunctions = ImmutableList.copyOf(functions);
			log.info("战役类型配置加载完成");
		} catch (Exception e) {
			log.info("战役类型配置加载失败");
		}
		
	}
	
	private void loadStarRewardConfig(){
		try {
			Map<Integer, ChallengeStarRewardTemplate> starRewardMap = Maps.newHashMap();
			List<ChallengeStarRewardTemplate> templateList = JSONUtil.fromJson(getJson(ChallengeStarRewardTemplate.class), new TypeReference<ArrayList<ChallengeStarRewardTemplate>>(){});
			for(ChallengeStarRewardTemplate template:templateList){
				template.init();
				starRewardMap.put(template.getIndex(),template);
			}
			this.starRewardMap = ImmutableMap.copyOf(starRewardMap);
			log.info("战役星级奖励配置加载完成");
		} catch (Exception e) {
			log.info("战役星级奖励配置加载失败");
		}
		
	}
	private void loadHistoryRewardConfig(){
		try {
			Map<Integer, DutyRoadRewardExtraTemplate> dutyRoadRewardMap = Maps.newHashMap();
			List<DutyRoadRewardExtraTemplate> templateList = JSONUtil.fromJson(getJson(DutyRoadRewardExtraTemplate.class), new TypeReference<ArrayList<DutyRoadRewardExtraTemplate>>(){});
			for(DutyRoadRewardExtraTemplate template:templateList){
				template.init();
				dutyRoadRewardMap.put(template.getFloor(),template);
			}
			this.dutyRoadRewardMap = ImmutableMap.copyOf(dutyRoadRewardMap);
			log.info("战役星级奖励配置加载完成");
		} catch (Exception e) {
			log.info("战役星级奖励配置加载失败");
		}
	}
	//加载夺宝奇兵配置
	private void loadTreasureTemplate() {
		try {
			Map<Integer, MissionTreasureTemplateImpl> tempMap = Maps.newHashMap();
			List<MissionTreasureTemplateImpl> templateList = JSONUtil.fromJson(getJson(MissionTreasureTemplateImpl.class), new TypeReference<ArrayList<MissionTreasureTemplateImpl>>(){});
			for(MissionTreasureTemplateImpl template:templateList){
				template.init();
				tempMap.put(template.getLevel(),template);
				if(this.treaureMaxLv<template.getLevel()) {
					this.treaureMaxLv = template.getLevel();
				}
			}
			this.treasureMap = ImmutableMap.copyOf(tempMap);
			log.info("夺宝奇兵配置加载完成");
		} catch (Exception e) {
			log.error("夺宝奇兵配置加载失败", e);
		}
	}
	public MissionExtraTemplate getMission(int missionId){
		return this.missionMap.get(missionId);
	}
	public List<Integer> getBattleHaveRecordReward(int type) {
		return this.battleMap.values().stream().filter(e -> e.getType() == type)
				.filter(e -> CollUtil.isNotEmpty(e.getRecordRewardList()))
				.map(e -> e.getId()).sorted().collect(Collectors.toList());
	}

	//是否花费石油
	public boolean isCostOil(int missionId) {
		MissionExtraTemplate template = getMission(missionId);
		if(template==null){
			return false;
		}
		return template.getNeed_oil()==1;
	}
	
	public MissionSpecailExtraTemplate getBattle(int id){
		return battleMap.get(id);
	}
	
	public MissionTypeTemplateImpl getMissionTypeTemplate(int type){
		return missionTypeMap.get(type);
	}
	
	public ExpeditionExtraTemplate getExpedition(int index, int lv){
		return expeditionMap.values().stream()
				.filter(c -> c.isFitLv(lv) && c.getIndex().equals(index))
				.findAny().orElse(null);
	}
	public BuyChanceTemplate getBuyChanceTemplate(int times){
		return buyChanceMap.getOrDefault(times, buyChanceMap.get(maxBuyTimes));
	}
	
	public ChallengeStarRewardTemplate getStarRewardTemplate(int id){
		return starRewardMap.get(id);
	}
	
	public DutyRoadRewardExtraTemplate getHistoryRewardTemplate(int id){
		return dutyRoadRewardMap.get(id);
	}
	//获取某个类型的战役的第一关id
	public int getFirstMissionId(int type){
		MissionSpecailExtraTemplate template = battleMap.values().stream().filter(t ->t.getType()==type&&t.getBefore_mission()==0).findFirst().orElse(null);
		if(template==null){
			return 0;
		}
		return template.getId();
	}
	//获取某个类型的战役的大关的第一关id
	public int getFirstMissionId(int type,int subType){
		MissionSpecailExtraTemplate template = battleMap.values().stream().filter(t ->t.getType()==type&&t.getChapter_id()==subType&&t.getBefore_mission()==0).findFirst().orElse(null);
		if(template==null){
			return 0;
		}
		return template.getId();
	}

	//检查是否需要开启战役
	public void checkBattleOpen(BasePlayer player){
		List<MissionTypeTemplateImpl> list=this.missionTypeMap.values().stream()
				.filter(t -> player.getPlayerFunction().isOpenFunction(t.getUnlock_level()))
				.filter(t -> !player.playerBattle().isUnlockBattle(t.getId()))
				.collect(Collectors.toList());
		for(MissionTypeTemplateImpl template:list){
			BattleType type= BattleType.getBattleType(template.getId());
			if(type != null) {
				BaseBattle battle = type.getPlayerBattle();
				if(battle!=null){
					player.playerBattle().unlockBattle(battle);
				}
			}
		}
	}
	
	public int getBuyTankTestCount(int type,int buyCount,int count){
		int cost = 0;
		for(int i=buyCount+1;i<=buyCount+count;i++){
			BuyChanceTemplate template = getBuyChanceTemplate(Math.min(i, buyChanceMap.size()));
			cost += type==0?template.getTank_test():template.getTank_test_SS();
		}
		return cost;
	}
	public MissionTreasureTemplateImpl getTreasureMission(int id) {
		return treasureMap.get(id);
	}
	/**
	 * 
	 * @param player
	 * @param index
	 * @param type 1-最小范围 2-最大范围
	 * @return
	 */
	public long getExpeditionMinCombat(Player player,int index,int type){
		ExpeditionExtraTemplate template = getExpedition(index, player.playerLevel().getLv());
		return template.getCombatRange(player,type);
	}
	public int getTreaureMaxLv() {
		return treaureMaxLv;
	}


	//获取主关卡对应的配置
	public MissionExtraTemplate getMainMission(int missionId){
		return this.missionMap.get(Math.min(missionId, MainMissionMaxId));
	}
	//获取战场寻宝保底奖励id
	public RecoveryBattleRewardTemplate getRecoverSecurityReward(int missionId){
		return recoveryRewardMap.values().stream().filter(t ->t.getLevel().intValue()==missionId&&t.getSecurity()>0).findAny().orElse(null);
	}
	//获取战场寻宝的奖励
	public List<Items> getRecoverReward(Player player,int missionId){
		//去除替换的保底奖励（保底奖励单独发放）
		/*RecoveryBattle battle = (RecoveryBattle) player.playerBattle().getPlayerBattle(BattleType.RecoveryBattle.getType());
		int exploreCount = battle.getExploreCount()+1;
		RecoveryBattleRewardTemplate securitytemplate = getRecoverSecurityReward(missionId);*/
		//先随机出奖励id
		int rewardId = this.recoveryRewardWeightMap.get(missionId).randomRatio();
		//如果有保底奖励则
		/*if(securitytemplate!=null&&exploreCount%securitytemplate.getSecurity()==0){
			rewardId = securitytemplate.getId();
		}*/
		RecoveryBattleRewardTemplate template = this.recoveryRewardMap.get(rewardId);
		if(template!=null){
			return template.getRewards();
		}
		return Lists.newArrayList();
	}
	//根据品质和玩家等级获取敌人
	public int getFrontBattleMissionId(int type,int playerLv){
		int lv = MathUtils.random(playerLv-5, playerLv+5);
		Optional<FrontBattleTemplate> optional = this.frontBattleMap.values().stream().filter(t ->t.isFit(type,lv)).findFirst();
		if(optional.isPresent()){
			return optional.get().getId();
		}
		return 1080;
	}


	public FrontBattleTemplate getFrontBattleMission(int missionId) {
		return frontBattleMap.get(missionId);
	}


	public MissionSpecailExtraTemplate getDayRewardMissionSpecialTemplate(int type, int id){
		MissionSpecailExtraTemplate template = battleMap.get(id);
		if (template != null){
			// 本关有每日奖励
			if (StrUtil.isNotEmpty(template.getDay_reward())){
				return template;
			}
			Map<Integer, MissionSpecailExtraTemplate> templateMap = battleTable.row(type);
			if (CollUtil.isNotEmpty(templateMap)){
				// 已通关的有奖励的最大关卡奖励
				return templateMap.values().stream()
						.filter(e -> e.getId() <= id)
						.filter(e -> StrUtil.isNotEmpty(e.getDay_reward()))
						.max(Comparator.comparing(MissionSpecailTemplate::getId))
						.orElse(null);
			}
		}
		return null;
	}

	public List<Integer> randomBuffIds(){
		return yuhunWeightMeta.randomEles(3);
	}

	public YuHunBuffTemplate getYuHunBuffTemplate(int id){
		return yuHunBuffMap.get(id);
	}

}