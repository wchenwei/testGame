package com.hm.util;

import com.google.common.collect.Lists;
import com.hm.config.GameConstants;

import java.util.List;

/**
 * 部队id处理器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/3 17:28
 */
public class TroopIDUtils {
    public static String buildTroopId(long playerId, int index) {
        return playerId + "_" + index;
    }

    public static int getTroopIndex(String troopId) {
        return Integer.parseInt(troopId.split("_")[1]);
    }

    public static long getPlayerId(String troopId) {
        return Integer.parseInt(troopId.split("_")[0]);
    }
}
