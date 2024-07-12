package com.hm.util.cos;

import com.google.common.collect.Lists;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.util.GameIdUtils;
import com.hm.model.war.FightDataRecord;
import com.hm.war.sg.WarResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RecordUtils {
	public static boolean saveDB = false;
	public static boolean saveCos = true;


	public static String getKfId() {
		String preFix = buildKey();
		return preFix + "kf_" + GameIdUtils.nextStrId();
	}

	public static String buildId(int serverId) {
		String preFix = buildKey();
		return preFix + serverId + "_" + GameIdUtils.nextStrId();
	}

	public static String buildKey() {
		return saveCos ? "cos_" + MongoUtils.getGameDBName() + "_" : "";
	}

	public static void saveCos(String id, List<WarResult> resultList) {
		if (saveCos) {
			CosUtils.uploadShowTime(id, resultList);
		}
	}

	public static void saveCos(String id, WarResult result) {
		if (saveCos) {
			CosUtils.uploadShowTime(id, Lists.newArrayList(result));
		}
	}
	public static void saveCosObj(String id, Object warResult) {
		if (saveCos) {
			CosUtils.uploadShowTime(id, warResult);
		}
	}
	
	public static void saveServerResult(String id,int serverId,WarResult result) {
		saveCos(id, result);
		if(saveDB) {
			new FightDataRecord(id, serverId, result).saveDb();
		}
	}
	
	
}
