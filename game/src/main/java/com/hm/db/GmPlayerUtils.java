package com.hm.db;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 判断玩家是否是gm
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/14 14:44
 */
public class GmPlayerUtils {
    public static Set<Long> playerIds = Sets.newHashSet();

    public static boolean isGm(long playerId) {
        return playerIds.contains(playerId);
    }

    public static void addGm(long playerId) {
        playerIds.add(playerId);
    }
}
