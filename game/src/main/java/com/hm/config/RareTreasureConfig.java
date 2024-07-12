package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.MissionSweeperEventTemplate;
import com.hm.config.excel.templaextra.RareTreasureBoxTemplate;
import com.hm.config.excel.templaextra.RareTreasureMapTemplate;
import com.hm.config.excel.templaextra.RareTreasureNpcTemplate;
import com.hm.config.excel.templaextra.RareTreasureTemplate;
import com.hm.util.MathUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:神秘宝藏配置
 * User: xjt
 * Date: 2020年3月16日17:51:31
 */

@Config
public class RareTreasureConfig extends ExcleConfig {
	//地图
    private Map<Integer, RareTreasureMapTemplate> map = Maps.newConcurrentMap();
    //npc配置
    private Map<Integer, RareTreasureNpcTemplate> npcMap = Maps.newConcurrentMap();
    //箱子配置
    private Map<Integer,RareTreasureBoxTemplate> boxs = Maps.newConcurrentMap();
    //
    private Table<Integer,Integer,RareTreasureTemplate> missions = HashBasedTable.create();
    //事件配置
    private Map<Integer,MissionSweeperEventTemplate> events = Maps.newConcurrentMap();
    
    
    @Override
    public void loadConfig() {
    	loadMapConfig();
    	loadNpcConfig();
    	loadBoxConfig();
    	loadMissionConfig();
    	loadEventConfig();
    }
    
    public void loadMapConfig(){
    	List<RareTreasureMapTemplate> list = JSONUtil.fromJson(getJson(RareTreasureMapTemplate.class), new TypeReference<List<RareTreasureMapTemplate>>() {
        });
        list.forEach(t ->t.init());
        map = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(RareTreasureMapTemplate::getId, Function.identity())));
    }
    
    public void loadNpcConfig(){
    	List<RareTreasureNpcTemplate> list = JSONUtil.fromJson(getJson(RareTreasureNpcTemplate.class), new TypeReference<List<RareTreasureNpcTemplate>>() {
        });
    	list.forEach(t ->t.init());
    	npcMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(RareTreasureNpcTemplate::getId, Function.identity())));
    }
    
    public void loadBoxConfig(){
    	List<RareTreasureBoxTemplate> list = JSONUtil.fromJson(getJson(RareTreasureBoxTemplate.class), new TypeReference<List<RareTreasureBoxTemplate>>() {
        });
    	list.forEach(t ->t.init());
    	boxs = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(RareTreasureBoxTemplate::getId, Function.identity())));
    }
    
    public void loadMissionConfig(){
    	List<RareTreasureTemplate> list = JSONUtil.fromJson(getJson(RareTreasureTemplate.class), new TypeReference<List<RareTreasureTemplate>>() {
        });
    	Table<Integer,Integer,RareTreasureTemplate> missions = HashBasedTable.create();
    	for(RareTreasureTemplate template : list){
    		template.init();
    		missions.put(template.getType(), template.getLevel(), template);
    	}
    	list.forEach(t ->t.init());
    	this.missions = ImmutableTable.copyOf(missions);
    }
    public void loadEventConfig(){
    	List<MissionSweeperEventTemplate> list = JSONUtil.fromJson(getJson(MissionSweeperEventTemplate.class), new TypeReference<List<MissionSweeperEventTemplate>>() {
        });
    	events = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(MissionSweeperEventTemplate::getId, Function.identity())));
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(RareTreasureMapTemplate.class,RareTreasureNpcTemplate.class,RareTreasureBoxTemplate.class,RareTreasureTemplate.class,MissionSweeperEventTemplate.class);
    }
    
    public RareTreasureTemplate getMission(int bigId,int id){
    	return missions.get(bigId, id);
    }
    
    public RareTreasureMapTemplate getMap(int id){
    	return this.map.get(id);
    }
    
    public int getRandomBoxId(int bigId,int id,int type){
    	List<Integer> boxIds = boxs.values().stream().filter(t->t.getLevel_b()==bigId&&t.getLevel_s()==id&&t.getType()==type).map(t->t.getId()).collect(Collectors.toList());
    	return boxIds.get(MathUtils.random(0, boxIds.size()));
    }

	public int getRandomEnemyId(int bigId, int id, int type) {
		List<Integer> enemyIds = npcMap.values().stream().filter(t->t.getLevel_b()==bigId&&t.getLevel_s()==id&&t.getType()==type).map(t->t.getId()).collect(Collectors.toList());
    	return enemyIds.get(MathUtils.random(0, enemyIds.size()));
	}

	public RareTreasureBoxTemplate getBox(int id) {
		return boxs.get(id);
	}
	
	public RareTreasureNpcTemplate getNpc(int id) {
		return npcMap.get(id);
	}
	
	public MissionSweeperEventTemplate getEvent(int id) {
		return events.get(id);
	}
}
