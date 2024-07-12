package com.hm.model.cityworld.troop.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.cityworld.troop.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Type;

/**
 * @Description: 城池部队转换
 * @author siyunlong  
 * @date 2019年11月21日 上午10:41:36 
 * @version V1.0
 */
@Slf4j
public class CityTroopConverter implements JsonDeserializer<BaseCityFightTroop>{

	@Override
	public BaseCityFightTroop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		try {
			int jt = json.getAsJsonObject().get("jt").getAsInt();
			if(jt == TroopJsonType.ClonePlayer) {
				return GSONUtils.FromJSONString(json.toString(), ClonePlayerTroop.class);
			}
			if(jt == TroopJsonType.PlayerCityTroop) {
				return GSONUtils.FromJSONString(json.toString(), PlayerCityTroop.class);
			}
			if(jt == TroopJsonType.CityDefNpcTroop) {
				return GSONUtils.FromJSONString(json.toString(), CityDefNpcTroop.class);
			}
			log.error("troop-json找不到:"+jt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
