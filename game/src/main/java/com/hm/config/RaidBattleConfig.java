package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ModernWarEventTemplate;
import com.hm.config.excel.templaextra.RaidBattleBossTemplate;
import com.hm.config.excel.templaextra.RaidBattleEventTemplate;
import com.hm.config.excel.templaextra.RaidBattleTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.RaidBattleEventType;
import com.hm.model.weight.RandomRatio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config
public class RaidBattleConfig extends ExcleConfig{
	private Map<Integer,RaidBattleTemplate> raidMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,RaidBattleBossTemplate> missionTable = HashBasedTable.create();
	private Map<Integer,RaidBattleEventTemplate> eventMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
		loadRaidBattleConfig();
		loadRaidBattleMissionConfig();
		loadEventConfig();
	}
	
	public void loadRaidBattleConfig(){
		List<RaidBattleTemplate> templateList = JSONUtil.fromJson(getJson(RaidBattleTemplate.class), new TypeReference<ArrayList<RaidBattleTemplate>>(){});
		templateList.forEach(t -> t.init());
		Map<Integer,RaidBattleTemplate> raidMap = templateList.stream().collect(Collectors.toMap(RaidBattleTemplate::getId, e ->e));
		this.raidMap = ImmutableMap.copyOf(raidMap);
	}
	
	public void loadRaidBattleMissionConfig(){
		Table<Integer,Integer,RaidBattleBossTemplate> missionTable = HashBasedTable.create();
		List<RaidBattleBossTemplate> templateList = JSONUtil.fromJson(getJson(RaidBattleBossTemplate.class), new TypeReference<ArrayList<RaidBattleBossTemplate>>(){});
		for(RaidBattleBossTemplate template:templateList){
			template.init();
			missionTable.put(template.getType(), template.getStage(), template);
		}
		this.missionTable = ImmutableTable.copyOf(missionTable);
	}
	
	
	public void loadEventConfig(){
		List<RaidBattleEventTemplate> templateList = JSONUtil.fromJson(getJson(RaidBattleEventTemplate.class), new TypeReference<ArrayList<RaidBattleEventTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer,RaidBattleEventTemplate> eventMap = templateList.stream().collect(Collectors.toMap(RaidBattleEventTemplate::getId, e ->e));
		this.eventMap = ImmutableMap.copyOf(eventMap);
	}
	//获取第一关
	public int getFirstId(){
		return raidMap.values().stream().max(Comparator.comparing(RaidBattleTemplate::getId)).get().getId();
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(RaidBattleTemplate.class,RaidBattleBossTemplate.class,ModernWarEventTemplate.class,RaidBattleEventTemplate.class);
	}

	public RaidBattleTemplate getRaidBattle(int id) {
		return this.raidMap.get(id);
	}
	//根据大关小关和已经生成的事件来生成新的事件
	public int createEvent(int id,int subId,Map<Integer,Integer> map){
		RaidBattleBossTemplate template = getMission(id,subId);
		int bigStage = template.getBig_stage();//该事件处于哪个阶段
		//该阶段已经出现的箱子数
		int boxNum = getBoxEventNum(id,map,bigStage);
		//已经出现加血的事件数量
		int addHpNum = getHpEventNum(map, RaidBattleEventType.Hp);
		//已经出现减血的事件数量
		int trapNum = getHpEventNum(map, RaidBattleEventType.Trap);
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		//箱子限制次数
		int boxLimit = commValueConfig.getRaidBattleBoxLimit(bigStage);
		//加血限制次数
		int hpLimit = commValueConfig.getCommValue(CommonValueType.RaidBattleHpLimit);
		//陷阱限制次数
		int trapLimit = commValueConfig.getCommValue(CommonValueType.RaidBattleTrapLimit);
		List<Integer> filterEventIds = Lists.newArrayList();
		if(boxNum>=boxLimit) filterEventIds.addAll(getEventIds(RaidBattleEventType.Package.getType()));
		if(addHpNum>=hpLimit) filterEventIds.addAll(getEventIds(RaidBattleEventType.Hp.getType()));
		if(trapNum>=trapLimit) filterEventIds.addAll(getEventIds(RaidBattleEventType.Trap.getType()));
		if(!filterEventIds.isEmpty()){
			RandomRatio newRatio = new RandomRatio(template.getEvent_library(),filterEventIds);
			return newRatio.randomRatio();
		}
		return template.getEventRatio().randomRatio();
	}
	//获取某个阶段礼包的数量
	private int getBoxEventNum(int id,Map<Integer, Integer> map, int stage) {
		int num = 0;
		for(Map.Entry<Integer, Integer> entry:map.entrySet()){
			RaidBattleBossTemplate template = getMission(id,entry.getKey());
			if(template.getStage()==stage&&getEvent(entry.getValue()).getType()==RaidBattleEventType.Package.getType()){
				num++;
			}
		}
		return num;
	}
	
	//已经出现的加血/减血事件的数量
	private int getHpEventNum(Map<Integer, Integer> map,RaidBattleEventType type){
		return (int) map.values().stream().filter(t ->getEvent(t).getType()==type.getType()).count();
	}

	public RaidBattleBossTemplate getMission(int id,int subId){
		return missionTable.get(id, subId);
	}
	
	public RaidBattleEventTemplate getEvent(int id){
		return eventMap.get(id);
	}
	public RaidBattleTemplate getBattleTemplate(int id){
		return raidMap.get(id);
	}
	
	public List<Integer> getEventIds(int type){
		return this.eventMap.values().stream().filter(t ->t.getType()==type).map(t ->t.getId()).collect(Collectors.toList());
	}
}
