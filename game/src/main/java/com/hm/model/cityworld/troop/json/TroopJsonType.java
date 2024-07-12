package com.hm.model.cityworld.troop.json;

import com.hm.model.cityworld.CityTroop;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class TroopJsonType {

	public static final int ClonePlayer = 1;
	public static final int CityDefNpcTroop = 2;
	public static final int PlayerCityTroop = 6;
	
	public static void firstBuildWorldCityTroop(List<WorldCity> cityList) {
		for (WorldCity worldCity : cityList) {
			firstBuildWorldCityTroop(worldCity);
		}
	}
	
	public static void firstBuildWorldCityTroop(WorldCity worldCity) {
		firstBuildWorldCityTroop(worldCity.getAtkCityTroop());
		firstBuildWorldCityTroop(worldCity.getDefCityTroop());
	}
	
	public static void firstBuildWorldCityTroop(CityTroop cityTroop) {
		if(cityTroop == null) {
			return;
		}
		for (BaseCityFightTroop cityFightTroop : cityTroop.getTroopList()) {
			if(cityFightTroop == null) {
				continue;
			}
			if(cityFightTroop instanceof ClonePlayerTroop) {
				cityFightTroop.setJt(ClonePlayer);
			}else if(cityFightTroop instanceof PlayerCityTroop) {
				cityFightTroop.setJt(PlayerCityTroop);
			}else if(cityFightTroop instanceof CityDefNpcTroop) {
				cityFightTroop.setJt(CityDefNpcTroop);
			}else {
				try {
					log.error("找不到troop:"+cityFightTroop.getClass().getSimpleName());
				} catch (Exception e) {
				}
			}
		}
	}
}
