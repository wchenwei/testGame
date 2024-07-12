/**  
 * Project Name:SLG_GameHot.
 * File Name:PushConfig.java  
 * Package Name:com.hm.config.excel  
 * Date:2018年9月29日上午9:28:55  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.PlayerMedalExtraTemplate;
import com.hm.config.excel.templaextra.PlayerMedalSpecialExtraTemplate;
import com.hm.config.excel.templaextra.PlayerMedalSpecialStarExtraTemplate;
import com.hm.config.excel.templaextra.PlayerMedalSpecialUpgradeExtraTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import com.google.common.collect.*;
/**
 * @Description: 玩家勋章
 * @author siyunlong  
 * @date 2019年9月2日 下午2:12:12 
 * @version V1.0
 */
@Config
public class PlayerMedalConfig extends ExcleConfig {
	private Map<Integer,PlayerMedalExtraTemplate> medalMap = Maps.newHashMap();
	private PlayerMedalExtraTemplate initMedalTemplate;//初始勋章id
	// 特殊勋章
	private Map<Integer, PlayerMedalSpecialExtraTemplate> medalSpecialMap = Maps.newConcurrentMap();
	private Map<Integer, PlayerMedalSpecialStarExtraTemplate> medalSpecialStarMap = Maps.newConcurrentMap();
	private Map<Integer, PlayerMedalSpecialUpgradeExtraTemplate> medalSpecialUpgradeMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,PlayerMedalSpecialStarExtraTemplate> medalSpecialStarTable = HashBasedTable.create();
	private Table<Integer,Integer,PlayerMedalSpecialUpgradeExtraTemplate> medalSpecialUpgradeTable = HashBasedTable.create();
	private Map<Integer,Integer> starMax = Maps.newConcurrentMap();
	private Map<Integer,Integer> lvMax = Maps.newConcurrentMap();
	private int medalMaxLv;

	@Override
	public void loadConfig() {
		loadRecaset();
		loadMedalSpecial();
		loadMedalSpecialStar();
		loadMedalSpecialUpgrade();
		loadMaxStarAndLv();
	}

	private void loadMedalSpecial() {
		Map<Integer,PlayerMedalSpecialExtraTemplate> medalMap = Maps.newHashMap();
		List<PlayerMedalSpecialExtraTemplate> list = JSONUtil.fromJson(getJson(PlayerMedalSpecialExtraTemplate.class), new TypeReference<List<PlayerMedalSpecialExtraTemplate>>() {
		});
		list.forEach(template ->{
			template.init();
			medalMap.put(template.getId(), template);
		});
		this.medalSpecialMap = ImmutableMap.copyOf(medalMap);
	}

	private void loadMedalSpecialStar() {
		Map<Integer,PlayerMedalSpecialStarExtraTemplate> medalMap = Maps.newHashMap();
		Table<Integer,Integer,PlayerMedalSpecialStarExtraTemplate> tempTable = HashBasedTable.create();
		ArrayListMultimap<Integer,PlayerMedalSpecialStarExtraTemplate> tempArrayListMultimap = ArrayListMultimap.create();
		List<PlayerMedalSpecialStarExtraTemplate> list = JSONUtil.fromJson(getJson(PlayerMedalSpecialStarExtraTemplate.class), new TypeReference<List<PlayerMedalSpecialStarExtraTemplate>>() {
		});
		list.forEach(template ->{
			template.init();
			medalMap.put(template.getId(), template);
			tempTable.put(template.getMedal_id(), template.getMedal_lv(), template);
			tempArrayListMultimap.put(template.getMedal_id(),template);
		});
		this.medalSpecialStarMap = ImmutableMap.copyOf(medalMap);
		this.medalSpecialStarTable = tempTable;
		medalMap.values().forEach(e -> e.reloadAttrMap(tempArrayListMultimap.get(e.getMedal_id())));
	}

	private void loadMedalSpecialUpgrade() {
		Map<Integer,PlayerMedalSpecialUpgradeExtraTemplate> medalMap = Maps.newHashMap();
		Table<Integer,Integer,PlayerMedalSpecialUpgradeExtraTemplate> tempTable = HashBasedTable.create();
		List<PlayerMedalSpecialUpgradeExtraTemplate> list = JSONUtil.fromJson(getJson(PlayerMedalSpecialUpgradeExtraTemplate.class), new TypeReference<List<PlayerMedalSpecialUpgradeExtraTemplate>>() {
		});
		list.forEach(template ->{
			template.init();
			medalMap.put(template.getId(), template);
			tempTable.put(template.getMedal_id(),template.getMedal_lv(),template);
		});
		this.medalSpecialUpgradeMap = ImmutableMap.copyOf(medalMap);
		this.medalSpecialUpgradeTable = tempTable;
	}

	private void loadMaxStarAndLv() {
		Map<Integer,Integer> starMaxMap = Maps.newConcurrentMap();
		Map<Integer,Integer> lvMaxMap = Maps.newConcurrentMap();
		medalSpecialMap.values().forEach(e ->{
			int maxStar = medalSpecialStarTable.row(e.getId()).keySet().stream().max(Comparator.comparing(Function.identity())).get();
			int maxLv = medalSpecialUpgradeTable.row(e.getId()).keySet().stream().max(Comparator.comparing(Function.identity())).get();
			starMaxMap.put(e.getId(),maxStar);
			lvMaxMap.put(e.getId(),maxLv);
		});
		this.starMax = ImmutableMap.copyOf(starMaxMap);
		this.lvMax = ImmutableMap.copyOf(lvMaxMap);
	}


	private void loadRecaset() {
		Map<Integer,PlayerMedalExtraTemplate> medalMap = Maps.newHashMap();
		for (PlayerMedalExtraTemplate template : loadPushConfig()) {
			template.init();
			medalMap.put(template.getId(), template);
		}
		medalMap.values().forEach(e -> e.loadMedalValue(medalMap));
		this.medalMap = ImmutableMap.copyOf(medalMap);
		this.initMedalTemplate = medalMap.values().stream().min(Comparator.comparing(PlayerMedalExtraTemplate::getId)).orElse(null);
		this.medalMaxLv = medalMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();
	}
	
	private List<PlayerMedalExtraTemplate> loadPushConfig() {
		return JSONUtil.fromJson(getJson(PlayerMedalExtraTemplate.class), new TypeReference<ArrayList<PlayerMedalExtraTemplate>>(){});
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(
				PlayerMedalExtraTemplate.class,
				PlayerMedalSpecialExtraTemplate.class,
				PlayerMedalSpecialStarExtraTemplate.class,
				PlayerMedalSpecialUpgradeExtraTemplate.class);
	}

	public PlayerMedalExtraTemplate getPlayerMedalTemplate(int id) {
		return medalMap.get(id);
	}

	public PlayerMedalExtraTemplate getInitMedalTemplate() {
		return initMedalTemplate;
	}

	public PlayerMedalSpecialExtraTemplate getPlayerMedalSpecialExtraTemplate(int id){
		return medalSpecialMap.get(id);
	}

	public PlayerMedalSpecialStarExtraTemplate getPlayerMedalSpecialStarExtraTemplate(int id){
		return medalSpecialStarMap.get(id);
	}

	public PlayerMedalSpecialUpgradeExtraTemplate getPlayerMedalSpecialUpgradeExtraTemplate(int id){
		return medalSpecialUpgradeMap.get(id);
	}

	public PlayerMedalSpecialStarExtraTemplate getPlayerMedalSpecialStarExtraTemplate(int medalId,int star){
		return medalSpecialStarTable.get(medalId, star);
	}

	public PlayerMedalSpecialUpgradeExtraTemplate getPlayerMedalSpecialUpgradeExtraTemplate(int medalId,int lv){
		return medalSpecialUpgradeTable.get(medalId, lv);
	}

	public int getMaxLv(int medalId){
		return lvMax.getOrDefault(medalId,0);
	}

	public int getMaxStar(int medalId){
		return starMax.getOrDefault(medalId,0);
	}

	public boolean isSpecialMedal(int id){
		return id > medalMaxLv;
	}
}


