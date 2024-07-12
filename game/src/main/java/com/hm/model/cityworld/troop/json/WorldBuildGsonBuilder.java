package com.hm.model.cityworld.troop.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;

/**
 * @Description:
 * @author siyunlong  
 * @date 2019年11月21日 上午10:45:28 
 * @version V1.0
 */
public class WorldBuildGsonBuilder {
	public static Gson gson;
	static {
		GsonBuilder gb = new GsonBuilder(); 
		gb.registerTypeAdapter(BaseCityFightTroop.class, new CityTroopConverter()); 
		gson = gb.create();
	}
	
	public static String toJson(WorldCity worldCity) {
		return GSONUtils.ToJSONString(worldCity);
	}
	
	public static WorldCity jsonToObject(String json) {
		return gson.fromJson(json, WorldCity.class);
	}
	
}
