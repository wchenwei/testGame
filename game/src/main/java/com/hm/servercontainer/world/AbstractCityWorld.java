package com.hm.servercontainer.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.CityConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.CityTroopType;
import com.hm.enums.CommonValueType;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.CityDefNpcTroop;
import com.hm.model.cityworld.troop.ClonePlayerTroop;
import com.hm.model.cityworld.troop.NpcCityTroop;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractCityWorld {
	private WorldType worldType;
	private int serverId;
	//此世界的城池列表
	protected Map<Integer,WorldCity> cityMap = Maps.newConcurrentMap();
	//初始化世界
	public abstract void initWorld();
	
	public AbstractCityWorld(int serverId,WorldType worldType) {
		this.serverId = serverId;
		this.worldType = worldType;
	}
	
	public MongodDB getMongodDB() {
		return MongoUtils.getMongodDB(serverId);
	}

	public WorldType getWorldType() {
		return worldType;
	}

	public int getServerId() {
		return serverId;
	}
	
	public WorldCity getWorldCity(int id) {
		if(id <= 0) {
			return null;
		}
		return cityMap.get(id);
	}
	
	public List<WorldCity> getWorldCitys() {
		return Lists.newArrayList(cityMap.values());
	}
	
	/**
	 * 获取每个城市的归属部落
	 * @return
	 */
	public Map<Integer,Integer> getWorldCityBelongGuild() {
		return getWorldCitys().stream()
				.filter(e -> e.getBelongGuildId() > 0)
				.collect(Collectors.toMap(WorldCity::getId, WorldCity::getBelongGuildId));
	}
	
	
	public void checkWorldCity(WorldCity worldCity) {
		CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		worldCity.setPortCity(cityTemplate.isPortCity());

		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int maxMorale = commValueConfig.getCommValue(CommonValueType.MoraleMax);
		TroopItemContainer troopItemContainer = TroopServerContainer.of(getServerId());
		//检查城市部队
		List<BaseCityFightTroop> allTroops = Lists.newArrayList();
		allTroops.addAll(worldCity.getAtkCityTroop().getTroopList());
		allTroops.addAll(worldCity.getDefCityTroop().getTroopList());
		allTroops.stream().forEach(troop -> {
			if(troop.getTroopType() == CityTroopType.NpcTroop.getType()) {
				NpcCityTroop npcTroop = (NpcCityTroop)troop;
				if(npcTroop.getState() == TroopState.PvpOneByOne.getType()) {
					npcTroop.setState(TroopState.None.getType());
				}
			}else if(troop.getTroopType() == CityTroopType.PlayerTroop.getType()) {
				String troopId = troop.getId();
				WorldTroop worldTroop = troopItemContainer.getWorldTroop(troopId);
				if(worldTroop == null || worldTroop.getCityId() != worldCity.getId()) {
					log.error(getServerId()+"部队:"+troopId+"出错在:"+worldCity.getId());
					worldCity.removeTroop(troopId);
				}
			}else if(troop.getTroopType() == CityTroopType.ClonePlayer.getType()) {
				ClonePlayerTroop tempTroop = (ClonePlayerTroop)troop;
				if(tempTroop.getState() == TroopState.PvpOneByOne.getType()) {
					tempTroop.setState(TroopState.None.getType());
				}
			}
			if(troop.getMoraleMax() <= 0) {
				troop.setMoraleMax(maxMorale);
			}
		});
		if(worldCity.getAtkCityTroop().Changed() || worldCity.getDefCityTroop().Changed()) {
			worldCity.saveDB();
		}
	}
	
	public List<WorldCity> getCityFromDB() {
		List<WorldCity> cityList =  RedisMapperUtil.queryListAll(getServerId(), WorldCity.class)
				.stream().filter(e -> e.getWorldType() == this.worldType).collect(Collectors.toList());
//		if(CollUtil.isNotEmpty(cityList)) {
//			return cityList;
//		}
//		List<WorldCity> oldCitys = getMongodDB().queryAll(WorldCity.class)
//				.stream().filter(e -> e.getWorldType() == this.worldType).collect(Collectors.toList());
//		WorldCityRedisUtils.updateWorldCityList(serverId, oldCitys);
//		List<WorldCity> newList = WorldCityRedisUtils.getWorldCity(getServerId())
//				.stream().filter(e -> e.getWorldType() == this.worldType).collect(Collectors.toList());
//		return newList;
		return cityList;
	}
	
	public void loadExtraCity(CityTemplate cityTemplate,WorldCity worldCity) {
		
	}
	
	public List<WorldCity> checkAndLoadCityFromDB() {
		NpcConfig npcConf = SpringUtil.getBean(NpcConfig.class);
		CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
		List<WorldCity> cityList = getCityFromDB();
		List<CityTemplate> templateList = cityConfig.getAllCityTemplate(this.worldType);
		
		if(cityList.size() < templateList.size()) {
			List<WorldCity> newCityList = Lists.newArrayList();
			List<CityTemplate> newList = templateList.stream().filter(cityTemplate -> {
				return cityList.stream().noneMatch(e -> e.getId() == cityTemplate.getId());
			}).collect(Collectors.toList());
			for (CityTemplate cityTemplate : newList) {
				WorldCity city = createWorldCity(cityTemplate, npcConf);
				loadExtraCity(cityTemplate, city);
				newCityList.add(city);
			}
			RedisMapperUtil.updateAll(serverId, newCityList, WorldCity.class);
			return getCityFromDB();
		}
		return cityList;
	}
	
	public WorldCity createWorldCity(CityTemplate cityTemplate,NpcConfig npcConf) {
		WorldCity city = new WorldCity();
		city.setId(cityTemplate.getId());
		city.setServerId(getServerId());
		int num = cityTemplate.getNpcNum();
		List<BaseCityFightTroop> npcList = npcConf.randomNpcList(city, num).stream()
				.map(e -> new CityDefNpcTroop(e,serverId,0)).collect(Collectors.toList());
		city.getCityNpc().setNpcIndex(0);
		city.getDefCityTroop().addNpcToTroop(npcList);
		return city;
	}
}
