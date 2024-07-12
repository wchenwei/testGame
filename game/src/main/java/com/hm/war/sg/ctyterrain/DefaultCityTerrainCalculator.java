package com.hm.war.sg.ctyterrain;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.CityConfig;
import com.hm.config.excel.templaextra.CityTemplate;

/**
 * @Description: 
 * @author siyunlong  
 * @date 2020年10月16日 下午5:59:30 
 * @version V1.0
 */
public class DefaultCityTerrainCalculator implements ICityTerrainCalculator{

	@Override
	public int calCityTerrain(int cityId) {
		if(cityId <= 0) {
			return 0;
		}
		CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		return cityTemplate != null?cityTemplate.getCity_terrain():0;
	}
	
}
