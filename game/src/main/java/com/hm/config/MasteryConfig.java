package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.TankAttrType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author xjt
 * @DATE 2019年7月2日10:48:33
 */
@Slf4j
@Config
public class MasteryConfig extends ExcleConfig{
	//升级消耗配置
	private Map<Integer,MasteryLvUpCostTemplate> lvUpCostMap = Maps.newConcurrentMap();
	//line配置
	private Map<Integer,MasteryLineTemplate> lineMap = Maps.newConcurrentMap();
	//光环配置
	private Map<Integer,MasteryLineEffectTemplate> cricleMap = Maps.newConcurrentMap();
	//专精配置
	private Map<Integer,MasteryEffectExtraTemplate> masteryMap = Maps.newConcurrentMap();
	
	private MasteryEffectExtraTemplate[][][] masterys = new MasteryEffectExtraTemplate[4][9][50];
	
	//连接配置
	private ListMultimap<Integer, List<Integer>> contentMap = ArrayListMultimap.create();
	//
	private int maxLv;
	
	@Override
	public void loadConfig() {
		loadMasteryConfig();
		loadLvUpCostConfig();
		loadLineConfig();
		loadConnectConfig();
		loadCricleMap();
	}
	private void loadCricleMap() {
		try {
			Map<Integer,MasteryLineEffectTemplate> cricleMap = Maps.newConcurrentMap();
			for(MasteryLineEffectTemplate template:JSONUtil.fromJson(getJson(MasteryLineEffectTemplate.class), new TypeReference<ArrayList<MasteryLineEffectTemplate>>(){})){
				template.init();
				cricleMap.put(template.getId(), template);
			}
			this.cricleMap = ImmutableMap.copyOf(cricleMap);
			log.info("专精光环加载完成");
		} catch (Exception e) {
			log.info("专精光环加载失败");
		}
		
		
	}
	//加载连接配置
	private void loadConnectConfig() {
		try {
			ListMultimap<Integer, List<Integer>> contentMap = ArrayListMultimap.create();
			for(MasteryConnectTemplate template:JSONUtil.fromJson(getJson(MasteryConnectTemplate.class), new TypeReference<ArrayList<MasteryConnectTemplate>>(){})){
				template.init();
				contentMap.put(template.getLocation(), template.getPoints());
			}
			this.contentMap = ImmutableListMultimap.copyOf(contentMap);
			log.info("专精连接加载完成");
		} catch (Exception e) {
			log.info("专精连接加载失败");
		}
		
	}
	//加载光环配置
	private void loadLineConfig() {
		try {
			Map<Integer,MasteryLineTemplate> lineMap = Maps.newConcurrentMap();
			for(MasteryLineTemplate template:JSONUtil.fromJson(getJson(MasteryLineTemplate.class), new TypeReference<ArrayList<MasteryLineTemplate>>(){})){
				template.init();
				lineMap.put(template.getId(), template);
			}
			this.lineMap = ImmutableMap.copyOf(lineMap);
			log.info("专精连线加载完成");
		} catch (Exception e) {
			log.info("专精连线加载失败");
		}
	}
	//加载升级消耗配置
	private void loadLvUpCostConfig() {
		try {
			Map<Integer,MasteryLvUpCostTemplate> lvUpCostMap = Maps.newConcurrentMap();
			for(MasteryLvUpCostTemplate template:JSONUtil.fromJson(getJson(MasteryLvUpCostTemplate.class), new TypeReference<ArrayList<MasteryLvUpCostTemplate>>(){})){
				template.init();
				lvUpCostMap.put(template.getId(), template);
				this.maxLv = Math.max(this.maxLv, template.getId());
			}
			this.lvUpCostMap = ImmutableMap.copyOf(lvUpCostMap);
			log.info("专精消耗加载完成");
		} catch (Exception e) {
			log.info("专精消耗加载失败");
		}
	}
	//加载专精配置
	public void loadMasteryConfig(){
		try {
			MasteryEffectExtraTemplate[][][] masterys = new MasteryEffectExtraTemplate[4][9][50];
			Map<Integer,MasteryEffectExtraTemplate> masteryMap = Maps.newConcurrentMap();
			for(MasteryEffectExtraTemplate template:JSONUtil.fromJson(getJson(MasteryEffectExtraTemplate.class), new TypeReference<ArrayList<MasteryEffectExtraTemplate>>(){})){
				template.init();
				masterys[template.getTank_type()-1][template.getLocation()-1][template.getLevel()-1] = template;
				masteryMap.put(template.getId(), template);
			}
			this.masterys = masterys;
			this.masteryMap = ImmutableMap.copyOf(masteryMap);
			log.info("专精配置加载完成");
		} catch (Exception e) {
			log.info("专精配置加载失败");
		}
	}
	
	public ListMultimap<Integer, List<Integer>> getConnect(){
		return contentMap;
	}
	
	public int getMaxLv(){
		return maxLv;
	}
	
	public MasteryLvUpCostTemplate getCostTemplate(int lv){
		return this.lvUpCostMap.get(lv);
	}
	public List<MasteryLineTemplate> getLines() {
		return Lists.newArrayList(this.lineMap.values());
	}
	//获取专精部位配置
	public MasteryEffectExtraTemplate getMasteryEffectTemplate(int type,int index,int lv){
		//return this.masteryMap.values().stream().filter(t ->t.getTank_type()==type&&t.getLocation()==index&&t.getLevel()==lv).findFirst().get();
		return this.masterys[type-1][index-1][lv-1];
	}
	
	//获取专精部位属性加成
	public Map<TankAttrType,Double> getMasteryAttrAdd(int type,int index,int lv){
		if(lv<=0){
			return Maps.newConcurrentMap();
		}
		MasteryEffectExtraTemplate template = getMasteryEffectTemplate(type, index, lv);
		if(template==null){
			return Maps.newConcurrentMap();
		}
		return template.getAttrMap();
	}
	//获取光环配置
	public MasteryLineEffectTemplate getMasteryCricle(int type,int id,int lv){
		return cricleMap.values().stream().filter(t -> t.isFit(type,id,lv)).findFirst().orElse(null);
	}

	//获取光环属性加成
	public Map<TankAttrType,Double> getMasteryCricleAttr(int type,int id,int lv){
		MasteryLineEffectTemplate template = getMasteryCricle(type, id, lv);
		if(template==null){
			return Maps.newConcurrentMap();
		}
		return template.getAttrMap();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(MasteryLvUpCostTemplate.class,MasteryLineTemplate.class,MasteryLineEffectTemplate.class,MasteryEffectExtraTemplate.class,MasteryConnectTemplate.class);
	}
	

}
