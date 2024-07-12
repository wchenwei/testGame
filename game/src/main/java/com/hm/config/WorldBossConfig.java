package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.WorldBossRankRewardTemplate;
import com.hm.config.excel.temlate.WorldBossTemplate;
import com.hm.config.excel.templaextra.WorldBossBoxRewardTemplate;
import com.hm.config.excel.templaextra.WorldBossExtraTemplate;
import com.hm.config.excel.templaextra.WorldBossHurtRewardTemplate;
import com.hm.config.excel.templaextra.WorldBossRankTemplate;
import com.hm.model.item.Items;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 
 * @author xjt
 * @DATE 2019年7月2日10:48:33
 */
@Slf4j
@Config
public class WorldBossConfig extends ExcleConfig{
	private Map<Integer,WorldBossExtraTemplate> bossMap = Maps.newConcurrentMap();
	private Map<Integer,WorldBossBoxRewardTemplate> boxMap = Maps.newConcurrentMap();
	private Table<Integer,Integer,WorldBossHurtRewardTemplate> hurtRewardTable = HashBasedTable.create();
	private List<WorldBossRankTemplate> rankList = Lists.newArrayList();
	@Override
	public void loadConfig() {
		loadBossMap();
		loadBoxMap();
		loadHurtReward();
		loadBossRank();
	}
	private void loadBossMap() {
		try {
			Map<Integer,WorldBossExtraTemplate> bossMap = Maps.newConcurrentMap();
			List<WorldBossExtraTemplate> templates = JSONUtil.fromJson(getJson(WorldBossExtraTemplate.class), new TypeReference<ArrayList<WorldBossExtraTemplate>>(){});
			templates.forEach(t ->t.init());
			bossMap = templates.stream().collect(Collectors.toMap(WorldBossTemplate::getId, t ->t));
			this.bossMap = ImmutableMap.copyOf(bossMap);
			log.info("加载世界boss配置完成");
		} catch (Exception e) {
			log.info("加载世界boss配置失败");
		}
	}
	
	private void loadBoxMap() {
		try {
			Map<Integer,WorldBossBoxRewardTemplate> boxMap = Maps.newConcurrentMap();
			List<WorldBossBoxRewardTemplate> templates = JSONUtil.fromJson(getJson(WorldBossBoxRewardTemplate.class), new TypeReference<ArrayList<WorldBossBoxRewardTemplate>>(){});
			templates.forEach(t ->t.init());
			boxMap = templates.stream().collect(Collectors.toMap(WorldBossBoxRewardTemplate::getId, t ->t));
			this.boxMap = ImmutableMap.copyOf(boxMap);
			log.info("加载世界boss箱子奖励配置完成");
		} catch (Exception e) {
			log.info("加载世界boss箱子奖励配置失败");
		}
	}
	
	private void loadHurtReward() {
		try {
			Table<Integer,Integer,WorldBossHurtRewardTemplate> hurtRewardTable = HashBasedTable.create();
			List<WorldBossHurtRewardTemplate> templates = JSONUtil.fromJson(getJson(WorldBossHurtRewardTemplate.class), new TypeReference<ArrayList<WorldBossHurtRewardTemplate>>(){});
			for(WorldBossHurtRewardTemplate template:templates){
				template.init();
				hurtRewardTable.put(template.getBoss(), template.getRate(), template);
			}
			this.hurtRewardTable = ImmutableTable.copyOf(hurtRewardTable);
			log.info("加载世界boss伤害奖励配置完成");
		} catch (Exception e) {
			log.info("加载世界boss伤害奖励配置失败");
		}
	}
	
	private void loadBossRank(){
		List<WorldBossRankTemplate> list = ImmutableList.copyOf(JSONUtil.fromJson(getJson(WorldBossRankTemplate.class), new TypeReference<ArrayList<WorldBossRankTemplate>>(){}));
		list.forEach(e -> e.init());
		this.rankList = list;
		log.info("加载世界boss排行配置完成");
	}
	
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(WorldBossExtraTemplate.class,WorldBossBoxRewardTemplate.class,WorldBossHurtRewardTemplate.class,WorldBossRankRewardTemplate.class);
	}
	
	public WorldBossHurtRewardTemplate getHurtTemplate(int lv,int id){
		return this.hurtRewardTable.get(lv, id);
	}
	public WorldBossExtraTemplate getBoss(int lv){
		return bossMap.get(lv);
	}
	
	public long getBossHp(int lv) {
		WorldBossExtraTemplate template = getBoss(lv);
		if(template==null){
			return 10000000;
		}
		return template.getHp();
	}
	public WorldBossBoxRewardTemplate getBossBox(int lv){
		return this.boxMap.get(lv);
	}
	
	public List<Items> getBossBoxReward(int lv){
		WorldBossBoxRewardTemplate template = getBossBox(lv);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewards();
	}
	public int getMaxLv() {
		return Collections.max(bossMap.keySet());
	}
	public List<Items> getRankRewards(int lv, int rank) {
		WorldBossRankTemplate template = rankList.stream().filter(t ->t.isFit(Math.max(1, lv), rank)).findAny().orElse(null);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getRewardList();
	}
	
	public int getMaxRank(){
		return this.rankList.stream().mapToInt(t ->t.getRank_up()).max().getAsInt();
	}
	
	public List<Items> getBossKillRewards(int lv){
		WorldBossExtraTemplate template = getBoss(lv);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getKillRewards();
	}
	
	

}
