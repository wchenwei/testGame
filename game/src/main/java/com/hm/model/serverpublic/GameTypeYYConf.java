package com.hm.model.serverpublic;

import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.util.PubFunc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "servertypemanage")
public class GameTypeYYConf {
	@Field("_id")
	private int typeId;
	@Field("extraMap")
	private Map<String,String> infoMap = Maps.newHashMap();
	
	public static Map<String,String> loadGameTypeYYConf(int typeId) {
		GameTypeYYConf result = MongoUtils.getLoginMongodDB().get(typeId, GameTypeYYConf.class);
		return result != null?result.getInfoMap():null;
	}

    public int getKfWorldWarId() {
        return PubFunc.parseInt(this.infoMap.get("kfworldwarId"));
	}
}
