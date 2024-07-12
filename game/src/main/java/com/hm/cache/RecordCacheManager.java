package com.hm.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hm.model.war.BattleRecord;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 *
 * @author siyunlong
 * @version V1.0
 * @Description: 战斗记录
 * @date 2023/10/28 20:47
 */
@Slf4j
public class RecordCacheManager {
	private static final RecordCacheManager instance = new RecordCacheManager();
	public static RecordCacheManager getInstance() {
		return instance;
	}

	protected static final String CACHESPEC = "expireAfterWrite=3600m";
	private final LoadingCache<String, BattleRecord> cache;
	private final BattleRecord DefaultValue = new BattleRecord();

	private RecordCacheManager() {
		cache = CacheBuilder.from(CACHESPEC)
			.maximumSize(10000)
			.build(new CacheLoader<String, BattleRecord>() {
			@Override
			public BattleRecord load(String key) {
				BattleRecord temp = getRecordFromDB(key);
				return temp != null?temp:DefaultValue;
			}
		});
	}
	public BattleRecord getRecord(String id) {
		BattleRecord temp = cache.getUnchecked(id);
		if(temp != DefaultValue) {
			return temp;
		}
		return null;
	}
	
	public void addRecord(BattleRecord record) {
		cache.put(record.getId(), record);
	}
	private static BattleRecord getRecordFromDB(String id) {
		return MongoUtils.getLoginMongodDB().get("id",BattleRecord.class,"KFBattleRecord");
	}

	private static void saveRecord(BattleRecord record) {
		MongoUtils.getLoginMongodDB().save(record,"KFBattleRecord");
	}
}
