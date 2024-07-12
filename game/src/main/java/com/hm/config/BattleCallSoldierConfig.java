package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.ActiveTankOrderConfigTemplate;
import com.hm.config.excel.temlate.ActiveTankOrderMissionTemplate;
import com.hm.config.excel.templaextra.ActiveBattleCallSoliderTemplate;
import com.hm.config.excel.templaextra.ActiveTankOrderBoxTemplateImpl;
import com.hm.config.excel.templaextra.ActiveTankOrderRewardExtraTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
@Config
public class BattleCallSoldierConfig extends ExcleConfig{
	private Map<Integer,ActiveBattleCallSoliderTemplate> missionMap = Maps.newConcurrentMap();
	private Map<Integer,ActiveTankOrderMissionTemplate> monsterMap = Maps.newConcurrentMap();
	private List<ActiveTankOrderRewardExtraTemplate> orderRewardList = Lists.newArrayList();
	private List<ActiveTankOrderBoxTemplateImpl> tankOrderBoxList = Lists.newArrayList();
	@Override
	public void loadConfig() {
		loadMission();
		loadMoster();
		loadOrderReward();
		loadTankOrderBox();
	}


	private void loadMoster() {
		List<ActiveTankOrderMissionTemplate> templateList = JSONUtil.fromJson(getJson(ActiveTankOrderMissionTemplate.class), new TypeReference<ArrayList<ActiveTankOrderMissionTemplate>>(){});
		this.monsterMap = ImmutableMap.copyOf(templateList.stream().collect(Collectors.toMap(ActiveTankOrderMissionTemplate::getId, Function.identity())));
	}

	private void loadOrderReward() {
		List<ActiveTankOrderRewardExtraTemplate> templateList = JSONUtil.fromJson(getJson(ActiveTankOrderRewardExtraTemplate.class), new TypeReference<ArrayList<ActiveTankOrderRewardExtraTemplate>>(){});
		templateList.forEach(e->e.init());
		this.orderRewardList = ImmutableList.copyOf(templateList);
	}

	private void loadTankOrderBox() {
		List<ActiveTankOrderBoxTemplateImpl> templateList = JSONUtil.fromJson(getJson(ActiveTankOrderBoxTemplateImpl.class), new TypeReference<ArrayList<ActiveTankOrderBoxTemplateImpl>>(){});
		templateList.forEach(e->e.init());
		this.tankOrderBoxList = ImmutableList.copyOf(templateList);
	}

	private void loadMission() {
		List<ActiveBattleCallSoliderTemplate> templateList = JSONUtil.fromJson(getJson(ActiveBattleCallSoliderTemplate.class), new TypeReference<ArrayList<ActiveBattleCallSoliderTemplate>>(){});
		templateList.forEach(t ->t.init());
		this.missionMap = ImmutableMap.copyOf(templateList.stream().collect(Collectors.toMap(ActiveTankOrderConfigTemplate::getId, Function.identity())));
	}
	
	public ActiveBattleCallSoliderTemplate getMission(int id){
		return this.missionMap.get(id);
	}
	public int getFirstMissionId(){
		return this.missionMap.keySet().stream().sorted().findFirst().get();
	}
	public ActiveTankOrderMissionTemplate getMonster(int id){
		return this.monsterMap.get(id);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveTankOrderConfigTemplate.class,ActiveTankOrderMissionTemplate.class,ActiveTankOrderRewardExtraTemplate.class,ActiveTankOrderBoxTemplateImpl.class);
	}

	public int createMonster(Player player,int curId) {
		int lv = player.playerLevel().getLv();
		ActiveTankOrderConfigTemplate template =  getMission(curId);
		if(template ==null){
			return 1;
		}
		return lv+template.getLevel_range();
	}

	public int getNextMissionId(int curId) {
		return getMission(curId).getLevel_next();
	}
	
	public int getBattleDoublePrice(int id,int times){
		ActiveBattleCallSoliderTemplate template = getMission(id);
		if(template==null){
			return 0;
		}
		return template.getPrice(times);
	}

	public List<Items> getReward(int playerLv, int curId){
		ActiveTankOrderRewardExtraTemplate template = orderRewardList.stream().filter(e -> e.isFit(curId, playerLv)).findFirst().orElse(null);
		if(Objects.isNull(template)){
			return null;
		}
		List<Items> rewardList = template.getRewardList();
		return rewardList;
	}

	public int getBattleCallSoldierSoulNum(int playerLv, int curId){
		ActiveTankOrderRewardExtraTemplate template = orderRewardList.stream().filter(e -> e.isFit(curId, playerLv)).findFirst().orElse(null);
		if(Objects.isNull(template)){
			return 0;
		}
		return template.getDriver_num();
	}

	public ActiveTankOrderBoxTemplateImpl getTankOrderBox(int playerLv, int id){
		ActiveTankOrderBoxTemplateImpl activeTankOrderBoxTemplate = this.tankOrderBoxList.stream().filter(e -> e.getId() == id && e.isFit(playerLv)).findFirst().orElse(null);
		return activeTankOrderBoxTemplate;
	}

}
