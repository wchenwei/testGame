/**  
 * Project Name:SLG_GameHot.
 * File Name:MailConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年4月10日下午3:03:16  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.lv.LvAgent;
import com.hm.config.excel.templaextra.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 纪念馆
 * @author siyunlong  
 * @date 2018年12月10日 下午7:59:08 
 * @version V1.0
 */
@Slf4j
@Config
public class MemorialHallConfig extends ExcleConfig {
	private MemorialChapterTemplate[] chapters;
	private Map<Integer,MemorialWallTemplate> wallMap = Maps.newHashMap();
	private MemorialWallTemplate[][] chapterWalls;
	private LvAgent<MemorialLvTemplate> lvAgent;
	private Map<Integer,PhotoTemplate> photoMap = Maps.newHashMap();
	private MemorialBuffTemplate[][] buffArrays;
	
	
	@Override
	public void loadConfig() {
		loadMemorialChapterTemplate();
		loadMemorialWallTemplate();
		loadMemorialLv();
		loadPhotoTemplate();
		loadMemorialBuffTemplate();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MemorialWallTemplate.class,MemorialChapterTemplate.class
				,MemorialLvTemplate.class,PhotoTemplate.class,MemorialBuffTemplate.class);
	}
	
	public void loadMemorialChapterTemplate() {
		List<MemorialChapterTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(MemorialChapterTemplate.class), new TypeReference<ArrayList<MemorialChapterTemplate>>(){}));
		int maxType = list.stream().mapToInt(e -> e.getId()).max().orElse(0);
		MemorialChapterTemplate[] types = new MemorialChapterTemplate[maxType];
		for (MemorialChapterTemplate template : list) {
			template.init();
			types[template.getId()-1] = template;
		}
		this.chapters = types;
	}
	
	public void loadMemorialWallTemplate() {
		List<MemorialWallTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(MemorialWallTemplate.class), new TypeReference<ArrayList<MemorialWallTemplate>>(){}));
		list.forEach(e -> e.init());
		Map<Integer,MemorialWallTemplate> wallMap = list
				.stream().collect(Collectors.toMap(MemorialWallTemplate::getId, e -> e));
		this.wallMap = ImmutableMap.copyOf(wallMap);
		
		int maxChapterId = Arrays.stream(chapters).mapToInt(e -> e.getId()).max().orElse(0);
		int maxWallId = list.stream().mapToInt(e -> e.getWallIndex()).max().orElse(0);
		MemorialWallTemplate[][] chapterWalls = new MemorialWallTemplate[maxChapterId][maxWallId];
		for (MemorialWallTemplate template : list) {
			getMemorialChapterTemplate(template.getChapter()).addMemorialWallTemplate(template);
			chapterWalls[template.getChapter()-1][template.getWallIndex()-1] = template;
		}
		this.chapterWalls = chapterWalls;
	}
	
	private void loadMemorialLv(){
		List<MemorialLvTemplate> templateList = JSONUtil.fromJson(getJson(MemorialLvTemplate.class), new TypeReference<ArrayList<MemorialLvTemplate>>(){});
		this.lvAgent = new LvAgent<>(templateList, true);
		log.info("纪念馆等级加载完成");
	}
	
	public void loadPhotoTemplate() {
		List<PhotoTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(PhotoTemplate.class), new TypeReference<ArrayList<PhotoTemplate>>(){}));
		Map<Integer,PhotoTemplate> wallMap = list
				.stream().collect(Collectors.toMap(PhotoTemplate::getPaper_id, e -> e));
		list.forEach(e -> e.init());
		this.photoMap = ImmutableMap.copyOf(wallMap);
	}
	
	public void loadMemorialBuffTemplate() {
		List<MemorialBuffTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(MemorialBuffTemplate.class), new TypeReference<ArrayList<MemorialBuffTemplate>>(){}));
		list.forEach(e -> e.init());
		
		int maxLv = list.stream().mapToInt(e -> e.getLevel()).max().orElse(0);
		int maxQuaty = list.stream().mapToInt(e -> e.getQuality()).max().orElse(0);
		MemorialBuffTemplate[][] buffArrays = new MemorialBuffTemplate[maxQuaty][maxLv];
		for (MemorialBuffTemplate template : list) {
			buffArrays[template.getQuality()-1][template.getLevel()-1] = template;
		}
		this.buffArrays = buffArrays;
	}

	public MemorialChapterTemplate[] getChapters() {
		return chapters;
	}
	
	public MemorialWallTemplate getMemorialWallTemplate(int id) {
		return this.wallMap.get(id);
	}
	public MemorialChapterTemplate getMemorialChapterTemplate(int id) {
		return this.chapters[id-1];
	}
	
	public MemorialWallTemplate getMemorialWallTemplate(int chapterId,int wallIndex) {
		return this.chapterWalls[chapterId-1][wallIndex-1];
	}
	
	public MemorialLvTemplate getMemorialLvTemplate(int lv){
		return this.lvAgent.getLevelValue(lv);
	}
	public PhotoTemplate getPhotoTemplate(int id){
		return this.photoMap.get(id);
	}
	
	public int calNewLv(long exp) {
		return this.lvAgent.getLevel(exp);
	}
	public int getLvMax(){
		return this.lvAgent.getMaxLv(); 
	}
	public MemorialBuffTemplate getMemorialBuffTemplate(int quality,int lv) {
		return this.buffArrays[quality-1][lv-1];
	}
}






