package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.MissionPeakNpcTemplate;
import com.hm.config.excel.templaextra.PeakBattleBoxTemplate;
import com.hm.config.excel.templaextra.PeakBattleLevelTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:邀请有礼
 * User: xjt
 * Date: 2019年4月26日09:14:01
 */

@Config
public class PeakBattleConfig extends ExcleConfig {
    private Map<Integer, PeakBattleLevelTemplate> levelMap = Maps.newConcurrentMap();
    private Map<Integer,MissionPeakNpcTemplate> npcMap = Maps.newConcurrentMap();
    private Map<Integer,PeakBattleBoxTemplate> boxMap = Maps.newConcurrentMap();
    @Override
    public void loadConfig() {
    	loadLevelConfig();
    	loadNpcConfig();
    	loadBoxRewardConfig();
    }
    
    public void loadLevelConfig(){
    	List<PeakBattleLevelTemplate> list = JSONUtil.fromJson(getJson(PeakBattleLevelTemplate.class), new TypeReference<List<PeakBattleLevelTemplate>>() {
        });
        list.forEach(t ->t.init());
        levelMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(PeakBattleLevelTemplate::getId, Function.identity())));
    }
    
    public void loadNpcConfig(){
    	List<MissionPeakNpcTemplate> list = JSONUtil.fromJson(getJson(MissionPeakNpcTemplate.class), new TypeReference<List<MissionPeakNpcTemplate>>() {
        });
    	npcMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(MissionPeakNpcTemplate::getId, Function.identity())));
    }
    
    public void loadBoxRewardConfig(){
    	List<PeakBattleBoxTemplate> list = JSONUtil.fromJson(getJson(PeakBattleBoxTemplate.class), new TypeReference<List<PeakBattleBoxTemplate>>() {
        });
    	list.forEach(t ->t.init());
    	boxMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(PeakBattleBoxTemplate::getId, Function.identity())));
    }

    
    public PeakBattleLevelTemplate getLevelTemplate(int id) {
    	return this.levelMap.get(id);
    }
    public int randomEnemyId(int lv){
    	PeakBattleLevelTemplate template = getLevelTemplate(lv);
    	if(template==null){
    		return 1;
    	}
    	return template.randomNpc();
    }
    

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(PeakBattleLevelTemplate.class,MissionPeakNpcTemplate.class,PeakBattleBoxTemplate.class);
    }
    //获取第一关id
	public int getFirstId() {
		return levelMap.keySet().stream().min(Comparator.comparing(Function.identity())).get();
	}

	public PeakBattleBoxTemplate getBoxTemplate(int id) {
		return boxMap.get(id);
	}
	//获取最后一关
	public int getEndId() {
		return levelMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();
	}

    public List<PeakBattleBoxTemplate> getBoxTemplateList(int totalStar) {
        return boxMap.values().stream().filter(e->e.getStar() <= totalStar).collect(Collectors.toList());
    }

}
