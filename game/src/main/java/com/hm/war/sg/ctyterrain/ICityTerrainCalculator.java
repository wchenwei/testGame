package com.hm.war.sg.ctyterrain;

/**
 * @Description: 
 * @author siyunlong  
 * @date 2020年10月16日 下午5:59:30 
 * @version V1.0
 */
public interface ICityTerrainCalculator {
	/**
	 * 根据城池id计算城池地形
	 * @param cityId
	 * @return
	 */
	int calCityTerrain(int cityId);
}
