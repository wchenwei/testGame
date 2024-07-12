package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.TotalWarMatchNewTemplate;
import com.hm.config.excel.temlate.TotalWarMatchTemplate;
import com.hm.config.excel.templaextra.OverallWarRewardTemplate;
import com.hm.config.excel.templaextra.OverallWarTemplate;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Config
public class OverallWarConfig extends ExcleConfig {
	private Map<Integer,OverallWarTemplate> npcMap = Maps.newConcurrentMap();
	private Map<Integer,OverallWarRewardTemplate> rewardMap = Maps.newConcurrentMap();
	private Map<Integer,TotalWarMatchTemplate> matchMap = Maps.newConcurrentMap();
	//新的匹配规则和得分规则，老规则保留
	private Map<Integer,TotalWarMatchNewTemplate> matchNewMap = Maps.newConcurrentMap();
	
	//
	private int maxGrade;
	private int maxNpcGrade;
	@Override
	public void loadConfig() {
		loadNpc();
		loadReward();
		loadMatch();
		loadMatchNew();
	}
	private void loadNpc(){
		Map<Integer,OverallWarTemplate> npcMap = Maps.newConcurrentMap();
		for(OverallWarTemplate template:JSONUtil.fromJson(getJson(OverallWarTemplate.class), new TypeReference<ArrayList<OverallWarTemplate>>(){})){
			template.init();
			npcMap.put(template.getId(), template);
		}
		this.npcMap = ImmutableMap.copyOf(npcMap);
	}
	private void loadReward(){
		Map<Integer,OverallWarRewardTemplate> rewardMap = Maps.newConcurrentMap();
		for(OverallWarRewardTemplate template:JSONUtil.fromJson(getJson(OverallWarRewardTemplate.class), new TypeReference<ArrayList<OverallWarRewardTemplate>>(){})){
			template.init();
			rewardMap.put(template.getId(), template);
		}
		this.rewardMap = ImmutableMap.copyOf(rewardMap);
	}
	private void loadMatch(){
		Map<Integer,TotalWarMatchTemplate> matchMap = Maps.newConcurrentMap();
		for(TotalWarMatchTemplate template:JSONUtil.fromJson(getJson(TotalWarMatchTemplate.class), new TypeReference<ArrayList<TotalWarMatchTemplate>>(){})){
			matchMap.put(template.getId(), template);
		}
		this.matchMap = ImmutableMap.copyOf(matchMap);
	}
	
	private void loadMatchNew(){
		Map<Integer,TotalWarMatchNewTemplate> matchNewMap = Maps.newConcurrentMap();
		for(TotalWarMatchNewTemplate template:JSONUtil.fromJson(getJson(TotalWarMatchNewTemplate.class), new TypeReference<ArrayList<TotalWarMatchNewTemplate>>(){})){
			matchNewMap.put(template.getId(), template);
			this.maxGrade = template.getType()==1?Math.max(this.maxGrade, template.getId()):this.maxGrade;
			this.maxNpcGrade = template.getType()==2?Math.max(this.maxNpcGrade, template.getId()):this.maxNpcGrade;
		}
		this.matchNewMap = ImmutableMap.copyOf(matchNewMap);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(OverallWarTemplate.class,OverallWarRewardTemplate.class,TotalWarMatchTemplate.class,TotalWarMatchNewTemplate.class);
	}
	
	public int randomNpc(){
		List<Integer> npcIds = npcMap.keySet().stream().collect(Collectors.toList());
		return npcIds.get(MathUtils.random(0, npcIds.size()));
	}
	public OverallWarTemplate getNpc(int id){
		return npcMap.get(id);
	}
	public OverallWarRewardTemplate getReward(int id){
		return rewardMap.get(id);
	}
	/**
	 * 
	 * @param player
	 * @param id
	 * @param type 1:最小范围 2：最大范围
	 * @return
	 */
	public long getMatchCombat(Player player,int id,int type){
		TotalWarMatchTemplate template = matchMap.get(id);
		if(template==null){
			return -1;
		}
		String[] rangStr = template.getRange().split("_");
		double range = type==1?Double.parseDouble(rangStr[0]):Double.parseDouble(rangStr[1]);
		return (long) (player.getCombat()*range);
	}
	
	public Map<Integer,TotalWarMatchNewTemplate> getMathchNew(){
		return matchNewMap;
	}
	
	public TotalWarMatchNewTemplate getMathchNew(int id){
		return matchNewMap.get(id);
	}
	public int getMaxGrade() {
		return maxGrade;
	}
	public int getMaxNpcGrade() {
		return maxNpcGrade;
	}
	//创建档位
	public Map<Integer,List<Integer>> createGrades(List<Integer> userIds){
		Map<Integer,List<Integer>> map = Maps.newConcurrentMap();
		getMathchNew().values().stream().filter(t ->t.getId()<=maxGrade).forEach(t ->{
			String[] rangStr = t.getRange().split("_");
			int from = Integer.parseInt(rangStr[0]);
			int end = Integer.parseInt(rangStr[1]);
			map.put(t.getId(), userIds.subList(Math.min(from,userIds.size()), Math.min(end,userIds.size()))); 
		});
		return map;
	}

}
