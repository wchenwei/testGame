package com.hm.config.city;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.*;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.CityConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerMission;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.util.dijkstras.Graph;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractWorldConfig {
	protected Map<Integer,CityTemplate> cityMap = Maps.newConcurrentMap();
	protected List<CityTemplate> cityList = Lists.newArrayList();
	protected Graph worldGraph;

	@Getter
	protected Map<Integer,CityAreaTemplate> areaMap = Maps.newHashMap();
	protected Table<Integer,Integer,List<Integer>> wayTable = HashBasedTable.create();
	//key:距离柏林的距离 value:所有此距离的城池列表
	protected ImmutableListMultimap<Integer, Integer> berlinMap;
	
	public abstract void initWorldConfig(CityConfig cityConfig);
	
	public CityTemplate getCityById(int id){
		return cityMap.get(id);
	}
	
	public List<CityTemplate> getAllCityTemplate() {
		return cityList;
	}

    public List<Integer> getWayCityList(int start,int end) {
    	return worldGraph.getShortestPath(end, start);
    }

	//获取两点之间的最短路线
    public List<Integer> getShotWay(int startId,int endId){
    	return this.wayTable.get(startId, endId);
    }
    
    //获取距离柏林distance 的所有城池
    public List<Integer> getBerlinCityList(int distance) {
    	return this.berlinMap.get(distance);
    }
    
    public List<Integer> getAllCityIds() {
    	return Lists.newArrayList(this.cityMap.keySet());
    }
	public CityAreaTemplate getCityAreaTemplate(int id) {
		return areaMap.get(id);
	}
    
    //两个城市是否相连
  	public boolean isConnect(int startCityId,int endCityId){
  		CityTemplate startTemplate = getCityById(startCityId);
  		if(startTemplate==null){
  			return false;
  		}
  		CityTemplate endTemplate = getCityById(startCityId);
  		if(endTemplate==null){
  			return false;
  		}
  		return startTemplate.getLinkCityIds().contains(endCityId);
  	}
    //获取玩家可通过的最短路径
	public List<Integer> getShotWay(Player player,int startId,int endId){
    	PlayerMission playerMission = player.playerMission();
    	final int openCity = playerMission.getOpenCity();
    	List<Integer> ways = getShotWay(startId, endId);
    	/*if(CollUtil.isEmpty(ways)) {
    		return Lists.newArrayList();
    	}*/
    	if(!CollUtil.isEmpty(ways)&&ways.stream().allMatch(e -> openCity >= e)) {
    		return ways;//所有都解锁了
    	}
    	//重新建立图谱
    	Graph playerGraph = new Graph();
    	this.cityList.stream().filter(e -> openCity >= e.getId())
    		.forEach(temp -> playerGraph.addNeighborById(temp.getId(), temp.getLinkCityIds().stream().filter(cityId ->openCity>=cityId).collect(Collectors.toList())));
    	return playerGraph.getShortestPath(endId, startId);
    }
	
	//获取玩家可通过的最短路径(不包含中间可以战斗的城)
	public List<Integer> getShotWayGuildBarrack(Player player,int startId,int endId){
    	//重新建立图谱
    	Graph playerGraph = new Graph();
    	this.cityList.stream().filter(e -> {
    		return isCanAdopt(player, e.getId(), endId);
    	}).forEach(temp -> playerGraph.addNeighborById(temp.getId(), temp.getLinkCityIds().stream().filter(cityId ->{
    		return isCanAdopt(player, cityId, endId);
    	}).collect(Collectors.toList())));
    	return playerGraph.getShortestPath(endId, startId);
    }
	/**
	 * 是否可以通过该城池，如果是目标城市则不需要判断是否有我方的战斗
	 * @param player
	 * @param cityId
	 * @param endId
	 * @return
	 */
	private boolean isCanAdopt(Player player,int cityId,int endId){
		int openCity = player.playerMission().getOpenCity();
		if(cityId>openCity){
			return false;
		}
    	TroopBiz troopBiz = SpringUtil.getBean(TroopBiz.class);
		if(cityId==endId){
			return true;
		}
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(worldCity==null){
			return false;
		}
		return !troopBiz.isEnterCityFight(player, worldCity);
	}
	
	 /**
     * 获取大型城市（不包含巴黎）
     * @return
     */
    public List<Integer> getBigCityIds() {
    	return cityMap.values().stream().filter(t ->t.isBigCity()).map(t ->t.getId()).collect(Collectors.toList());
    }
	
}
