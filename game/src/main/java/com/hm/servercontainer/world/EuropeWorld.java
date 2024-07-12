package com.hm.servercontainer.world;

import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.WorldType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.CityDefNpcTroop;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 欧洲世界
 * @author siyunlong  
 * @date 2019年3月11日 下午5:24:12 
 * @version V1.0
 */
@Slf4j
public class EuropeWorld extends AbstractCityWorld{
	public EuropeWorld(int serverId) {
		super(serverId,WorldType.Normal);
	}
	
	@Override
	public void initWorld() {
		for (WorldCity worldCity : checkAndLoadCityFromDB()) {
			checkWorldCity(worldCity);
			cityMap.put(worldCity.getId(), worldCity);
		}
		log.error("世界城池数:"+this.cityMap.size());
	}
	
	@Override
	public WorldCity createWorldCity(CityTemplate cityTemplate,NpcConfig npcConf) {
		WorldCity city = new WorldCity();
		city.setId(cityTemplate.getId());
		city.setServerId(getServerId());
		//加载正常npc
		int num = cityTemplate.getNpcNum();
		List<BaseCityFightTroop> npcList = npcConf.randomNpcList(city, num).stream()
				.map(e -> new CityDefNpcTroop(e,getServerId(),0)).collect(Collectors.toList());
		city.getCityNpc().setNpcIndex(0);
		city.getDefCityTroop().addNpcToTroop(npcList);
		return city;
	}
	
}
