package com.hm.config;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.FarWinMissiontTemplate;
import com.hm.config.excel.templaextra.FarWinRewardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Config
public class FarWinBattleConfig extends ExcleConfig {
	private Map<Integer,FarWinMissiontTemplate> missions = Maps.newConcurrentMap();
	private Map<Integer,FarWinRewardTemplate> mileRewards = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
		loadMissionConfig();
		loadMissionRewardConfig();
	}

	private void loadMissionConfig() {
		List<FarWinMissiontTemplate> templateList = JSONUtil.fromJson(getJson(FarWinMissiontTemplate.class), new TypeReference<ArrayList<FarWinMissiontTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer,FarWinMissiontTemplate> missions = templateList.stream().collect(Collectors.toMap(FarWinMissiontTemplate::getId, e ->e));
		this.missions = ImmutableMap.copyOf(missions);
	}

	private void loadMissionRewardConfig() {
		List<FarWinRewardTemplate> templateList = JSONUtil.fromJson(getJson(FarWinRewardTemplate.class), new TypeReference<ArrayList<FarWinRewardTemplate>>(){});
		templateList.forEach(t ->t.init());
		Map<Integer,FarWinRewardTemplate> mileRewards = templateList.stream().collect(Collectors.toMap(FarWinRewardTemplate::getId, e ->e));
		this.mileRewards = ImmutableMap.copyOf(mileRewards);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(FarWinMissiontTemplate.class,FarWinRewardTemplate.class);
	}
	
	public FarWinMissiontTemplate getMissionTemplate(int id){
		return this.missions.get(id);
	}
	
	public FarWinRewardTemplate getMileRewardTemplate(int id){
		return this.mileRewards.get(id);
	}
	
	public int getFirst(int type){
		return missions.values().stream().filter(t ->t.getType()==type).mapToInt(t ->t.getId()).min().getAsInt();
	}
	public List<Integer> getBattleHaveRecordReward(int type) {
		return this.missions.values().stream().filter(e -> e.getType() == type)
				.filter(e -> CollUtil.isNotEmpty(e.getRecordRewardList()))
				.map(e -> e.getId()).sorted().collect(Collectors.toList());
	}
}
