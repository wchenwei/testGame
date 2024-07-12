package com.hm.config.city;

import com.google.common.collect.*;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.WorldType;
import com.hm.util.dijkstras.Graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorldConfig extends AbstractWorldConfig{
	@Override
	public void initWorldConfig(CityConfig cityConfig) {
		loadCityConfig(cityConfig);
		loadWay();
	}
	
	private void loadCityConfig(CityConfig cityConfig){
		List<CityTemplate> allCityTemplate = cityConfig.getAllCityTemplate();
		Map<Integer,CityAreaTemplate> allAreaMap = cityConfig.getAreaMap();

		ArrayListMultimap<Integer, CityTemplate> cityAreaMap = ArrayListMultimap.create();
		this.worldGraph = new Graph();
		Map<Integer,CityTemplate> cityMap = Maps.newConcurrentMap();
		for(CityTemplate temp:allCityTemplate){
			if(temp.getMap() == WorldType.Normal.getType()) {
				cityMap.put(temp.getId(), temp);
				this.worldGraph.addNeighborById(temp.getId(), temp.getLinkCityIds());
				cityAreaMap.put(temp.getArea(), temp);
			}
		}
		this.cityList = ImmutableList.copyOf(Lists.newArrayList(cityMap.values()));
		this.cityMap = ImmutableMap.copyOf(cityMap);

		//加载区域
		Map<Integer,CityAreaTemplate> areaMap = Maps.newHashMap();
		for (CityAreaTemplate areaTemplate : allAreaMap.values()) {;
			if(areaTemplate.getMap_type() == WorldType.Normal.getType()) {
				areaTemplate.loadCityList(cityAreaMap.get(areaTemplate.getId()));
				areaMap.put(areaTemplate.getId(),areaTemplate);
			}
		}
		this.areaMap = ImmutableMap.copyOf(areaMap);
	}
	
	private void loadWay() {
		ArrayListMultimap<Integer, Integer> berlinMap = ArrayListMultimap.create();
		Table<Integer,Integer,List<Integer>> wayTable = HashBasedTable.create();
		cityMap.keySet().forEach(cityId ->{
			List<Integer> otherCityIds = cityMap.keySet().stream().filter(t -> t!=cityId).map(t ->new Integer(t)).collect(Collectors.toList());
			otherCityIds.forEach(otherId ->{
				List<Integer> wayList = worldGraph.getShortestPath(otherId, cityId);
				wayTable.put(cityId, otherId, wayList);
				if(cityId == GameConstants.PeaceId) {
					berlinMap.put(wayList.size()-1, otherId);
				}
			});
		});
		this.wayTable = ImmutableTable.copyOf(wayTable);
		this.berlinMap = ImmutableListMultimap.copyOf(berlinMap);
	}
}
