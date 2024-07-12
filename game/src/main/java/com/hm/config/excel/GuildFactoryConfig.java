package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

@Config
public class GuildFactoryConfig extends ExcleConfig {
	private Map<Integer, GuildFactoryBuildExtraTemplate> buildMap = Maps.newConcurrentMap();
	private Map<Integer, GuildFactoryLevelExtraTemplate> lvMap = Maps.newConcurrentMap();
	private Table<Integer,Integer, GuildFactoryWeaponUpgradeExtraTemplate> upgradeTable = HashBasedTable.create();
	private Map<Integer,ItemGuildWeaponExtraTemplate> weaponMap = Maps.newConcurrentMap();
	private Map<Integer,GuildFactoryPaperExtraTemplate> paperMap = Maps.newConcurrentMap();
	private int maxLv;
	@Override
	public void loadConfig() {
		loadBuildConfig();
		loadLevelConfig();
		loadWeaponConfig();
		loadWeaponUpgradeConfig();
		loadPaperConfig();
	}
	
	private void loadPaperConfig() {
		Map<Integer, GuildFactoryPaperExtraTemplate> map = Maps.newConcurrentMap();
		List<GuildFactoryPaperExtraTemplate> tempList = JSONUtil.fromJson(getJson(GuildFactoryPaperExtraTemplate.class), new TypeReference<ArrayList<GuildFactoryPaperExtraTemplate>>(){});
		for(GuildFactoryPaperExtraTemplate template:tempList) {
			template.init();
			map.put(template.getId(),template);
		}
		this.paperMap = ImmutableMap.copyOf(map);
	}

	private void loadWeaponConfig() {
		Map<Integer, ItemGuildWeaponExtraTemplate> map = Maps.newConcurrentMap();
		List<ItemGuildWeaponExtraTemplate> tempList = JSONUtil.fromJson(getJson(ItemGuildWeaponExtraTemplate.class), new TypeReference<ArrayList<ItemGuildWeaponExtraTemplate>>(){});
		for(ItemGuildWeaponExtraTemplate template:tempList) {
			template.init();
			map.put(template.getId(),template);
		}
		this.weaponMap = ImmutableMap.copyOf(map);
	}

	public void loadBuildConfig(){
		Map<Integer, GuildFactoryBuildExtraTemplate> map = Maps.newConcurrentMap();
		List<GuildFactoryBuildExtraTemplate> tempList = JSONUtil.fromJson(getJson(GuildFactoryBuildExtraTemplate.class), new TypeReference<ArrayList<GuildFactoryBuildExtraTemplate>>(){});
		for(GuildFactoryBuildExtraTemplate template:tempList) {
			template.init();
			map.put(template.getId(),template);
		}
		this.buildMap = ImmutableMap.copyOf(map);
	}
	
	public void loadLevelConfig(){
		Map<Integer, GuildFactoryLevelExtraTemplate> map = Maps.newConcurrentMap();
		List<GuildFactoryLevelExtraTemplate> tempList = JSONUtil.fromJson(getJson(GuildFactoryLevelExtraTemplate.class), new TypeReference<ArrayList<GuildFactoryLevelExtraTemplate>>(){});
		for(GuildFactoryLevelExtraTemplate template:tempList) {
			template.init();
			map.put(template.getId(),template);
			this.maxLv = Math.max(this.maxLv, template.getId());
		}
		this.lvMap = ImmutableMap.copyOf(map);
	}
	
	public void loadWeaponUpgradeConfig() {
		Table<Integer,Integer,GuildFactoryWeaponUpgradeExtraTemplate> tempTable = HashBasedTable.create();
		List<GuildFactoryWeaponUpgradeExtraTemplate> tempList = JSONUtil.fromJson(getJson(GuildFactoryWeaponUpgradeExtraTemplate.class), new TypeReference<ArrayList<GuildFactoryWeaponUpgradeExtraTemplate>>(){});
		tempList.forEach(e -> {
			e.init();
			tempTable.put(e.getWeapon_id(), e.getWeapon_level(), e);
		});
		this.upgradeTable = ImmutableTable.copyOf(tempTable);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(GuildFactoryBuildExtraTemplate.class, GuildFactoryLevelExtraTemplate.class, GuildFactoryWeaponUpgradeExtraTemplate.class,ItemGuildWeaponExtraTemplate.class,GuildFactoryPaperExtraTemplate.class);
	}
	
	public GuildFactoryBuildExtraTemplate getBuildTemplate(int type){
		return this.buildMap.get(type);
	}

	public GuildFactoryLevelExtraTemplate getLevel(int lv) {
		return this.lvMap.get(lv);
	}

	public GuildFactoryWeaponUpgradeExtraTemplate getWeaponUpgradeTemplate(int id, int lv) {
		return this.upgradeTable.get(id, lv);
	}
	
	public int getArmsMaxLv(int id){
		OptionalInt optional = this.upgradeTable.row(id).values().stream().mapToInt(t ->t.getWeapon_level()).max();
		if(optional.isPresent()){
			return optional.getAsInt();
		}
		return 0;
	}
	
	
	public ItemGuildWeaponExtraTemplate getWeapon(int id) {
		return this.weaponMap.get(id);
	}
	//获取武器升到lv级所需的经验
	public int getExpTotalLv(int id,int lv){
		GuildFactoryWeaponUpgradeExtraTemplate template = getWeaponUpgradeTemplate(id, lv);
		if(template==null){
			return 0;
		}
		return template.getExp_total();
	}
	
	public GuildFactoryPaperExtraTemplate getPaper(int id) {
		return this.paperMap.get(id);
	}

	public int getMaxLv() {
		return maxLv;
	}
	
	
}









