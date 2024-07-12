package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ModernWarEventTemplate;
import com.hm.config.excel.temlate.ModernWarTemplate;
import com.hm.config.excel.templaextra.ModernWarLevelExtraTemplate;
import com.hm.config.excel.templaextra.ModernWarMissionExtraTemplate;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config
public class ModernBattleConfig extends ExcleConfig{
	private Map<Integer,ModernWarTemplate> warMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,ModernWarLevelExtraTemplate> warLevelTable = HashBasedTable.create();
	private Map<Integer,ModernWarMissionExtraTemplate> missionMap = Maps.newConcurrentMap();
	private Map<Integer,ModernWarEventTemplate> eventMap = Maps.newConcurrentMap();
	@Override
	public void loadConfig() {
		loadWarConfig();
		loadWarLevelConfig();
		loadWarMissionConfig();
		loadEventConfig();
	}
	
	public void loadWarConfig(){
		List<ModernWarTemplate> templateList = JSONUtil.fromJson(getJson(ModernWarTemplate.class), new TypeReference<ArrayList<ModernWarTemplate>>(){});
		Map<Integer,ModernWarTemplate> warMap = templateList.stream().collect(Collectors.toMap(ModernWarTemplate::getId, e ->e));
		this.warMap = ImmutableMap.copyOf(warMap);
	}
	
	public void loadWarLevelConfig(){
		Table<Integer,Integer,ModernWarLevelExtraTemplate> warLevelTable = HashBasedTable.create();
		List<ModernWarLevelExtraTemplate> templateList = JSONUtil.fromJson(getJson(ModernWarLevelExtraTemplate.class), new TypeReference<ArrayList<ModernWarLevelExtraTemplate>>(){});
		for(ModernWarLevelExtraTemplate template:templateList){
			template.init();
			warLevelTable.put(template.getLevel(), template.getSub_level(), template);
		}
		this.warLevelTable = ImmutableTable.copyOf(warLevelTable);
	}
	
	public void loadWarMissionConfig(){
		List<ModernWarMissionExtraTemplate> templateList = JSONUtil.fromJson(getJson(ModernWarMissionExtraTemplate.class), new TypeReference<ArrayList<ModernWarMissionExtraTemplate>>(){});
		templateList.stream().forEach(t ->t.init());
		Map<Integer,ModernWarMissionExtraTemplate> missionMap = templateList.stream().collect(Collectors.toMap(ModernWarMissionExtraTemplate::getMission_id, e ->e));
		this.missionMap = ImmutableMap.copyOf(missionMap);
	}
	
	public void loadEventConfig(){
		List<ModernWarEventTemplate> templateList = JSONUtil.fromJson(getJson(ModernWarEventTemplate.class), new TypeReference<ArrayList<ModernWarEventTemplate>>(){});
		Map<Integer,ModernWarEventTemplate> eventMap = templateList.stream().collect(Collectors.toMap(ModernWarEventTemplate::getId, e ->e));
		this.eventMap = ImmutableMap.copyOf(eventMap);
	}
	//获取第一关
	public int getFirstId(){
		return warMap.values().stream().max(Comparator.comparing(ModernWarTemplate::getId)).get().getId();
	}
	//
	public ModernWarTemplate getModernWar(int id){
		return warMap.get(id);
	}
	public ModernWarLevelExtraTemplate getModernWarLevel(int id,int lv){
		return warLevelTable.get(id, lv);
	}
	//生成事件
	public int[] createEvents(int id,int progress){
		int[] eventIds = new int[]{0,0,0};
		ModernWarLevelExtraTemplate template = getModernWarLevel(id, progress);
		if(template==null){
			return eventIds;
		}
		int eventNum = template.randomEventNum();
		for(int i=1;i<=eventNum;i++){
			eventIds[i-1] = template.randomEventId();
		}
		return eventIds;
	}
	
	//生成一个事件
	public int createEvents(int id,int progress,int eventNum){
		ModernWarLevelExtraTemplate template = getModernWarLevel(id, progress);
		return template.randomEventId();
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ModernWarTemplate.class,ModernWarLevelExtraTemplate.class,ModernWarEventTemplate.class,ModernWarMissionExtraTemplate.class);
	}

	public ModernWarEventTemplate getEvent(int id) {
		return this.eventMap.get(id);
	}
	
	public ModernWarMissionExtraTemplate getMission(int id) {
		return this.missionMap.get(id);
	}
	
	public List<Items> getFightRewards(int id,int lv,int missionId){
		List<Items> rewards = Lists.newArrayList();
		ModernWarMissionExtraTemplate missionTemplate = getMission(missionId);
		if(missionTemplate!=null){
			rewards.addAll(missionTemplate.getRewards());
		}
		ModernWarLevelExtraTemplate template = getModernWarLevel(id, lv);
		if(template!=null){
			rewards.addAll(template.getRewards());
		}
		return rewards;
	}
}
