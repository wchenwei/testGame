package com.hm.redis.kftask;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.hm.libcore.util.gson.GSONUtils;

import java.util.List;

/**
 * 玩家跨服任务处理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/4/9 11:30
 */
public class KFPlayerTaskCache {
    private static Table<KFTaskType, Number, BaseKFTask> cache = HashBasedTable.create();

    public static void clearCache() {
        cache.clear();
    }

    public static <T extends BaseKFTask> T getKFPlayerTaskFromCache(KFTaskType type, Number playerId) {
        BaseKFTask val = cache.get(type, playerId);
        if (val == null) {
            val = getKFPlayerTaskFromDB(type, playerId);
            cache.put(type, playerId, val);
        }
        return (T) val;
    }

    public static <T extends BaseKFTask> T getKFPlayerTaskFromDB(KFTaskType type, Number playerId) {
        String key = type.buildKeyName();
        String json = BaseKFTask.getVal(key, playerId);
        if (StrUtil.isEmpty(json)) {
            return (T) type.create(playerId);
        }
        return GSONUtils.FromJSONString(json, type.toKFTaskClass());
    }

    public static Table<KFTaskType, Number, BaseKFTask> getCache() {
        return cache;
    }

    public static List<BaseKFTask> getPlayerTaskList(KFTaskType type) {
        return Lists.newArrayList(cache.row(type).values());
    }
}
