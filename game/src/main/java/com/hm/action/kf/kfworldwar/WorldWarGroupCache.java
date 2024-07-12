package com.hm.action.kf.kfworldwar;

import com.google.common.collect.Maps;
import com.hm.model.activity.kfactivity.KfWorldWarServerGroup;

import java.util.Map;

/**
 * 世界大战分组缓存
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/27 17:08
 */
public class WorldWarGroupCache {
    public static Map<Integer, KfWorldWarServerGroup> cacheMap = Maps.newConcurrentMap();

    public static void init() {
        cacheMap.clear();
        KfWorldWarServerGroup.getAllServerGroup(KfWorldWarServerGroup.class)
                .forEach(e -> addGroupToCache(e));
    }

    public static void addGroupToCache(KfWorldWarServerGroup group) {
        group.getServerIds().forEach(e -> cacheMap.put(e, group));
    }

    public static String getWarUrl(int teamId) {
        KfWorldWarServerGroup group = getWarServerGroup(teamId);
        if (group == null) {
            return null;
        }
        return group.getUrl();
    }

    public static KfWorldWarServerGroup getWarServerGroup(int teamId) {
        KfWorldWarServerGroup group = cacheMap.get(teamId);
        if (group == null) {
            group = KfWorldWarServerGroup.findFitKfServerGroup(teamId, KfWorldWarServerGroup.class);
            if (group == null) {
                return null;
            }
            addGroupToCache(group);
        }
        return group;
    }
}
