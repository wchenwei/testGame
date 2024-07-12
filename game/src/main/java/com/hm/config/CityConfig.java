package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.city.AbstractWorldConfig;
import com.hm.config.city.CityAreaTemplate;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.CityBaseTemplate;
import com.hm.config.excel.temlate.CityIsLandTemplate;
import com.hm.config.excel.templaextra.CityGuideTemplate;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.WorldType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Config
public class CityConfig extends ExcleConfig{
	protected Map<Integer,CityTemplate> cityMap = Maps.newConcurrentMap();
	@Getter
	protected Map<Integer,CityAreaTemplate> areaMap = Maps.newHashMap();
	protected Map<Integer,CityGuideTemplate> cityGuideMap = Maps.newConcurrentMap();
	private AbstractWorldConfig[] worldConfigs = new AbstractWorldConfig[WorldType.WorldList.length];


	@Override
	public void loadConfig() {
		loadCityConfig();
		loadCityGuideConfig();
		this.areaMap = json2ImmutableMap(CityAreaTemplate::getId,CityAreaTemplate.class);

		for (WorldType worldType : WorldType.WorldList) {
			AbstractWorldConfig config = worldType.createWorldConfig();
			config.initWorldConfig(this);
			this.worldConfigs[worldType.getType()] = config; 
		}
	}
	private void loadCityConfig(){
		List<CityTemplate> cityList = JSONUtil.fromJson(getJson(CityTemplate.class), new TypeReference<ArrayList<CityTemplate>>(){});
		cityList.forEach(e -> e.init());
		this.cityMap = ImmutableMap.copyOf(cityList.stream().collect(Collectors.toMap(CityTemplate::getId, e -> e)));
	}
	private void loadCityGuideConfig(){
		List<CityGuideTemplate> list = JSONUtil.fromJson(getJson(CityGuideTemplate.class), new TypeReference<ArrayList<CityGuideTemplate>>(){});
		list.forEach(e -> e.init());
		this.cityGuideMap = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(CityGuideTemplate::getId, e -> e)));
	}
	
	public AbstractWorldConfig getWorldConfig(WorldType worldType) {
		return this.worldConfigs[worldType.getType()];
	}
	

	public CityTemplate getCityById(int id){
		return this.cityMap.get(id);
	}

	public CityAreaTemplate getCityAreaTemplate(int id){
		return this.worldConfigs[WorldType.Normal.getType()].getCityAreaTemplate(id);
	}
	
	public List<CityTemplate> getAllCityTemplate() {
		return Lists.newArrayList(this.cityMap.values());
	}

	public Map<Integer, CityAreaTemplate> getAreaMap(WorldType worldType) {
		return this.worldConfigs[worldType.getType()].getAreaMap();
	}

	public List<CityTemplate> getAllCityTemplate(WorldType worldType) {
		return this.worldConfigs[worldType.getType()].getAllCityTemplate();
	}
	//两个城市是否相连
	public boolean isConnect(int startCityId,int endCityId){
		return this.worldConfigs[WorldType.getTypeByCityId(startCityId).getType()].isConnect(startCityId, endCityId);
	}
	
	//路线是否正确
	public boolean isFitWays(List<Integer> ways) {
		for (int i = 0; i < ways.size(); i++) {
			if(i==ways.size()-1){
				return true;
			}
			int startId = ways.get(i);
			int nextId = ways.get(i+1);
			if(!isConnect(startId, nextId)) {//判断相连
				return false;
			}
		}
		return true;
	}

    public CityGuideTemplate getCityGuide(int id) {
        return this.cityGuideMap.get(id);
    }

    public List<Integer> getBigCityIds(){
    	return this.worldConfigs[WorldType.Normal.getType()].getBigCityIds();
    }

    public List<Integer> getWayCityList(int start,int end) {
    	return this.worldConfigs[WorldType.getTypeByCityId(start).getType()].getWayCityList(start, end);
    }

	//获取两点之间的最短路线
    public List<Integer> getShotWay(int startId,int endId){
    	return this.worldConfigs[WorldType.getTypeByCityId(startId).getType()].getShotWay(startId, endId);
    }
    
    //获取距离柏林distance 的所有城池
    public List<Integer> getBerlinCityList(int distance) {
    	return this.worldConfigs[WorldType.Normal.getType()].getBerlinCityList(distance);
    }
    
    public List<Integer> getAllCityIds(WorldType worldType) {
    	return this.worldConfigs[worldType.getType()].getAllCityIds();
    }
    
    //获取玩家可通过的最短路径
	public List<Integer> getShotWay(Player player,int startId,int endId){
		return this.worldConfigs[WorldType.getTypeByCityId(startId).getType()].getShotWay(player,startId, endId);
    }
	
	//获取玩家可通过的最短路径(用于部落派兵，和挂机选择不同)
	public List<Integer> getShotWayGuildBarrack(Player player,int startId,int endId){
		return this.worldConfigs[WorldType.getTypeByCityId(startId).getType()].getShotWayGuildBarrack(player,startId, endId);
    }
	
	public int[] calCityTypeNum(List<Integer> cityList) {
		int[] types = new int[3];
		for (int cityId : cityList) {
			CityTemplate template = getCityById(cityId);
			if(template != null && template.getCity_type() > 0) {
				types[template.getCity_type() - 1]++;
			}
		}
		return types;
	}
	//获取城市政绩点
	public long getCityScore(List<Integer> citys){
		return citys.stream().mapToLong(t -> cityMap.get(t).getAchievements()).sum();
	}
	
    public static void main(String[] args) {
    	CityConfig config = new CityConfig();
    	config.loadConfig();
    	long now = System.currentTimeMillis();
    	/*System.err.println(config.getWayCityList(50, 13));
    	System.err.println(System.currentTimeMillis()-now);*/
    	System.err.println(config.getShotWay(17, 12));
    	System.err.println(config.getShotWay(17, 28));
    	
	}
}
