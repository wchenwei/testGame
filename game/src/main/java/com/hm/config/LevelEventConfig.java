package com.hm.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.MissionEventBuffTemplate;
import com.hm.config.excel.temlate.MissionEventWarTemplate;
import com.hm.config.excel.temlate.MissionLevelWarTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
@Config
public class LevelEventConfig extends ExcleConfig{
	private Map<Integer, LevelEventTemplate> levelEventMap = Maps.newHashMap(); 
	private Map<Integer, MissionLevelWarTemplate> levelEventWarMap = Maps.newHashMap();
	private Map<Integer, LevelEventRewardTemplate> levelEventRewardMap = Maps.newHashMap();
	
	private Map<Integer, LevelEventNewTemplate> levelEventNewMap = Maps.newHashMap();
	private Map<Integer, MissionEventWarTemplate> levelEventWarNewMap = Maps.newHashMap();
	private Map<Integer, LevelEventLinkTemplate> lines = Maps.newHashMap();
	private Map<Integer, LevelEventRewardNewTemplate> levelEventRewardNewMap = Maps.newHashMap();
	
	private Map<Integer,MissionEventBuffTemplate> buffs = Maps.newConcurrentMap();
	private Map<Integer, LevelEventBuffGroupTemplate> buffGroups = Maps.newConcurrentMap();
	
	@Override
	public void loadConfig() {
		loadLevelEventConfig();
		loadLevelEventWarConfig();
		loadLevelEventRewardConfig();

		loadLevelEventNewConfig();
		loadLevelEventWarNewConfig();
		loadLevelEventRewardNewConfig();
		loadLines();
		loadBuffs();
		loadBuffGroup();
	}

	private void loadBuffGroup() {
		List<LevelEventBuffGroupTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventBuffGroupTemplate.class), new TypeReference<ArrayList<LevelEventBuffGroupTemplate>>() {
		});
		templateList.forEach(t -> t.init());
		Map<Integer, LevelEventBuffGroupTemplate> map = templateList.stream().collect(Collectors.toMap(LevelEventBuffGroupTemplate::getId, Function.identity()));
		this.buffGroups = ImmutableMap.copyOf(map);
	}

	private void loadLevelEventRewardNewConfig() {
		Map<Integer, LevelEventRewardNewTemplate> levelEventWarMap = Maps.newHashMap();
		List<LevelEventRewardNewTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventRewardNewTemplate.class), new TypeReference<ArrayList<LevelEventRewardNewTemplate>>(){});
		for(LevelEventRewardNewTemplate template:templateList){
			template.init();
			levelEventWarMap.put(template.getId(), template);
		}
		this.levelEventRewardNewMap = ImmutableMap.copyOf(levelEventWarMap);
		
	}

	private void loadBuffs() {
		List<MissionEventBuffTemplate> templateList = JSONUtil.fromJson(getJson(MissionEventBuffTemplate.class), new TypeReference<ArrayList<MissionEventBuffTemplate>>(){});
		Map<Integer, MissionEventBuffTemplate> map = templateList.stream().collect(Collectors.toMap(MissionEventBuffTemplate::getId, Function.identity()));
		this.buffs = ImmutableMap.copyOf(map);
	}

	private void loadLines() {
		List<LevelEventLinkTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventLinkTemplate.class), new TypeReference<ArrayList<LevelEventLinkTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer, LevelEventLinkTemplate> map = templateList.stream().collect(Collectors.toMap(LevelEventLinkTemplate::getId, Function.identity()));
		this.lines = ImmutableMap.copyOf(map);
	}

	private void loadLevelEventWarNewConfig() {
		List<MissionEventWarTemplate> templateList = JSONUtil.fromJson(getJson(MissionEventWarTemplate.class), new TypeReference<ArrayList<MissionEventWarTemplate>>(){});
		Map<Integer, MissionEventWarTemplate> levelEventWarNewMap = templateList.stream().collect(Collectors.toMap(MissionEventWarTemplate::getWar_id, Function.identity()));
		this.levelEventWarNewMap = ImmutableMap.copyOf(levelEventWarNewMap);
		
	}

	private void loadLevelEventNewConfig() {
		List<LevelEventNewTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventNewTemplate.class), new TypeReference<ArrayList<LevelEventNewTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer, LevelEventNewTemplate> levelEventMap = templateList.stream().collect(Collectors.toMap(LevelEventNewTemplate::getMission_id, Function.identity()));
		this.levelEventNewMap = ImmutableMap.copyOf(levelEventMap);
		
	}

	private void loadLevelEventRewardConfig() {
		Map<Integer, LevelEventRewardTemplate> levelEventRewardMap = Maps.newHashMap(); 
		List<LevelEventRewardTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventRewardTemplate.class), new TypeReference<ArrayList<LevelEventRewardTemplate>>(){});
		for(LevelEventRewardTemplate template:templateList){
			template.init();
			levelEventRewardMap.put(template.getId(), template);
		}
		this.levelEventRewardMap = ImmutableMap.copyOf(levelEventRewardMap);
	}

	private void loadLevelEventConfig() {
		Map<Integer, LevelEventTemplate> levelEventMap = Maps.newHashMap(); 
		List<LevelEventTemplate> templateList = JSONUtil.fromJson(getJson(LevelEventTemplate.class), new TypeReference<ArrayList<LevelEventTemplate>>(){});
		for(LevelEventTemplate template:templateList){
			template.init();
			levelEventMap.put(template.getMission_id(), template);
		}
		this.levelEventMap = ImmutableMap.copyOf(levelEventMap);
	}

	private void loadLevelEventWarConfig() {
		Map<Integer, MissionLevelWarTemplate> levelEventWarMap = Maps.newHashMap();
		List<MissionLevelWarTemplate> templateList = JSONUtil.fromJson(getJson(MissionLevelWarTemplate.class), new TypeReference<ArrayList<MissionLevelWarTemplate>>(){});
		for(MissionLevelWarTemplate template:templateList){
			levelEventWarMap.put(template.getWar_id(), template);
		}
		this.levelEventWarMap = ImmutableMap.copyOf(levelEventWarMap);
	}
	public MissionLevelWarTemplate getLevelEventWar(int warId){
		return levelEventWarMap.get(warId);
	}
	public MissionEventWarTemplate getLevelEventWarNew(int warId){
		return levelEventWarNewMap.get(warId);
	}
	public LevelEventTemplate getLevelEvent(int id){
		return levelEventMap.get(id);
	}
	
	public LevelEventNewTemplate getLevelEventNew(int id){
		return levelEventNewMap.get(id);
	}
	public int getStartId(int warId){
		LevelEventTemplate template = levelEventMap.values().stream().filter(t -> t.getWar_id()==warId&&t.getStart_mission()==1).findFirst().orElse(null);
		if(template==null){
			return 0;
		}
		return template.getMission_id();
	}
	
	public int getNewStartId(int warId){
		LevelEventNewTemplate template = levelEventNewMap.values().stream().filter(t -> t.getWar_id()==warId&&t.getStart_mission()==1).findFirst().orElse(null);
		if(template==null){
			return 0;
		}
		return template.getMission_id();
	}
	public LevelEventRewardTemplate getReward(int id){
		return this.levelEventRewardMap.get(id);
	}
	//获取事件的所有奖励id
	public List<Integer> getRewardByWarId(int warId) {
		return this.levelEventRewardMap.values().stream().filter(t ->t.getWar_id()==warId).map(t ->t.getId()).collect(Collectors.toList());
	}
	
	//获取事件的所有奖励id
	public List<Integer> getNewRewardByWarId(int warId) {
		return this.levelEventRewardNewMap.values().stream().filter(t ->t.getWar_id()==warId).map(t ->t.getId()).collect(Collectors.toList());
	}
	public void checkLevelEventOpen(Player player){
		int lv = player.playerLevel().getLv();
		List <MissionLevelWarTemplate> list=this.levelEventWarMap.values().stream().filter(t -> {
			return lv>=t.getLevel()&&!player.playerLevelEvent().isUnlockEvent(t.getWar_id());
		}).collect(Collectors.toList());
		for(MissionLevelWarTemplate template:list){
			player.playerLevelEvent().unlockEvent(template.getWar_id());
		}
	}
	
	public void checkLevelEventNewOpen(Player player){
		int lv = player.playerLevel().getLv();
		List <MissionEventWarTemplate> list=this.levelEventWarNewMap.values().stream().filter(t -> {
			return lv>=t.getLevel()&&!player.playerEvent().isUnlockEvent(t.getWar_id());
		}).collect(Collectors.toList());
		for(MissionEventWarTemplate template:list){
			player.playerEvent().unlockEvent(template.getWar_id());
		}
		//检查是否开启了新的星级
		player.playerEvent().checkEvent();
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(LevelEventTemplate.class,MissionLevelWarTemplate.class
				,LevelEventRewardTemplate.class,LevelEventNewTemplate.class,MissionEventWarTemplate.class
				, LevelEventLinkTemplate.class, LevelEventRewardNewTemplate.class, MissionEventBuffTemplate.class
				, LevelEventBuffGroupTemplate.class);
	}

	public int getLine(int nowId, int id) {
		//根据两个点确定一条线
		LevelEventLinkTemplate template = this.lines.values().stream().filter(t ->t.isFit(nowId, id)).findFirst().orElse(null);
		return template==null?0:template.getId();
	}

	public int randomBuff(int type) {
		List<Integer> buffIds = this.buffs.values().stream().filter(t ->t.getType()==type).map(t ->t.getId()).collect(Collectors.toList());
		if(CollUtil.isEmpty(buffIds)) {
			return 0;
		}
		return RandomUtil.randomEle(buffIds);
	}
	
	public MissionEventBuffTemplate getBuff(int buffId) {
		return this.buffs.get(buffId);
	}
	public LevelEventRewardNewTemplate getRewardNew(int id){
		return this.levelEventRewardNewMap.get(id);
	}

	public int getAllLineNum(int warId) {
		return (int)lines.values().stream().filter(t ->t.getWar_id()==warId).count();
	}

	public int getAllCityNum(int warId) {
		return (int)levelEventWarMap.values().stream().filter(t->t.getWar_id()==warId).count();
	}

	public List<Items> getAllRewards(int warId) {
		List<Items> rewards = Lists.newArrayList();
		List<Integer> ids = getRewardByWarId(warId);
		ids.forEach(t ->{
			LevelEventRewardTemplate template = getReward(t);
			if(template!=null) {
				rewards.addAll(template.getRewards());
			}
		});
		return rewards;
	}

	public LevelEventLinkTemplate getLine(int id) {
		return this.lines.get(id);
	}

	public LevelEventBuffGroupTemplate getBuffGroup(int buffGroupId) {
		return buffGroups.get(buffGroupId);
	}

	;
	
}
