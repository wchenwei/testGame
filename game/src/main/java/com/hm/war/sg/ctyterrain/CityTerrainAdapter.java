package com.hm.war.sg.ctyterrain;

/**
 * @Description: 战斗城池地形计算器
 * @author siyunlong  
 * @date 2020年10月16日 下午5:59:17 
 * @version V1.0
 */
public class CityTerrainAdapter {
	private static ICityTerrainCalculator cityTerrainCalculator = new DefaultCityTerrainCalculator();
	
	public static void replaceCityTerrainCalculator(ICityTerrainCalculator newCityTerrainCalculator) {
		cityTerrainCalculator = newCityTerrainCalculator;
	}
	
	public static int calCityTerrain(int cityId) {
		if(cityId <= 0) {
			return 0;
		}
		try {
			return cityTerrainCalculator.calCityTerrain(cityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
