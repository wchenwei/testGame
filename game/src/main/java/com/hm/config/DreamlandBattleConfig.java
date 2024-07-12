package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.MissionDreamChapterTemplate;
import com.hm.config.excel.templaextra.MissionDreamBoxExtraTemplate;
import com.hm.config.excel.templaextra.MissionDreamEliteExtraTemplate;
import com.hm.config.excel.templaextra.MissionDreamExtraTemplate;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Config
public class DreamlandBattleConfig extends ExcleConfig {
	//大关卡map
	private Map<Integer,MissionDreamChapterTemplate> chapterMap = Maps.newConcurrentMap();
	//普通关卡
	private Map<Integer,MissionDreamExtraTemplate> missions = Maps.newConcurrentMap();
	//精英关卡
	private Map<Integer,MissionDreamEliteExtraTemplate> elites = Maps.newConcurrentMap();
	private Table<Integer,Integer,MissionDreamEliteExtraTemplate> eliteTable = HashBasedTable.create();
	//进度奖励
	private Map<Integer,MissionDreamBoxExtraTemplate> boxs = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		loadDreamlandConfig();
		loadChapterConfig();
		loadBoxConfig();
		loadElitesConfig();
	}

	private void loadElitesConfig() {
		Map<Integer,MissionDreamEliteExtraTemplate> elites= Maps.newConcurrentMap();
		Table<Integer,Integer,MissionDreamEliteExtraTemplate> eliteTable = HashBasedTable.create();
		List<MissionDreamEliteExtraTemplate> templateList = JSONUtil.fromJson(getJson(MissionDreamEliteExtraTemplate.class), new TypeReference<ArrayList<MissionDreamEliteExtraTemplate>>(){});
		for(MissionDreamEliteExtraTemplate template:templateList) {
			template.init();
			elites.put(template.getId(), template);
			eliteTable.put(template.getChapter_id(), template.getId(), template);
		}
		this.elites = ImmutableMap.copyOf(elites);
		this.eliteTable = ImmutableTable.copyOf(eliteTable);
	}

	private void loadBoxConfig() {
		List<MissionDreamBoxExtraTemplate> templateList = JSONUtil.fromJson(getJson(MissionDreamBoxExtraTemplate.class), new TypeReference<ArrayList<MissionDreamBoxExtraTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer,MissionDreamBoxExtraTemplate> boxs = templateList.stream().collect(Collectors.toMap(MissionDreamBoxExtraTemplate::getId, e ->e));
		this.boxs = ImmutableMap.copyOf(boxs);
		
	}

	private void loadChapterConfig() {
		List<MissionDreamChapterTemplate> templateList = JSONUtil.fromJson(getJson(MissionDreamChapterTemplate.class), new TypeReference<ArrayList<MissionDreamChapterTemplate>>(){});
		Map<Integer,MissionDreamChapterTemplate> chapterMap = templateList.stream().collect(Collectors.toMap(MissionDreamChapterTemplate::getId, e ->e));
		this.chapterMap = ImmutableMap.copyOf(chapterMap);
	}

	private void loadDreamlandConfig() {
		List<MissionDreamExtraTemplate> templateList = JSONUtil.fromJson(getJson(MissionDreamExtraTemplate.class), new TypeReference<ArrayList<MissionDreamExtraTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer,MissionDreamExtraTemplate> missions = templateList.stream().collect(Collectors.toMap(MissionDreamExtraTemplate::getId, e ->e));
		this.missions = ImmutableMap.copyOf(missions);
	}

	

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MissionDreamChapterTemplate.class,MissionDreamExtraTemplate.class,MissionDreamEliteExtraTemplate.class,MissionDreamBoxExtraTemplate.class);
	}
	//获取普通关卡的第一关
	public int getFirstId() {
		return missions.values().stream().mapToInt(t ->t.getId()).min().getAsInt();
	}

	public MissionDreamExtraTemplate getMission(int curId) {
		return missions.get(curId);
	}
	//获取某一大关的最后一关(普通关卡)
	public int getLastId(int chapterId) {
		return missions.values().stream().filter(t ->t.getChapter_id()==chapterId).mapToInt(t ->t.getId()).max().getAsInt();
	}

	public MissionDreamEliteExtraTemplate getElite(int id) {
		return elites.get(id);
	}

	public MissionDreamChapterTemplate getChapter(int id) {
		return chapterMap.get(id);
	}

	public MissionDreamBoxExtraTemplate getBox(int id) {
		return boxs.get(id);
	}
	//获取某一章节的所有关卡
	public List<Integer> getAllElite(int chapterId){
		return Lists.newArrayList(eliteTable.row(chapterId).keySet());
	}

	public List<Items> getSweepReward(int id) {
		List<Items> rewards = missions.values().stream().filter(t ->t.getChapter_id()==id).flatMap(t ->t.getRewards().stream()).collect(Collectors.toList());
		return rewards;
	}
	
	

}
