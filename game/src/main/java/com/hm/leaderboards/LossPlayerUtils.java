package com.hm.leaderboards;

import com.hm.config.GameConstants;
import com.hm.enums.RankType;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;

/**
 * 流失玩家功能判断
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/10/8 14:53
 */
public class LossPlayerUtils {
    public static int LossDay = 30;

    /**
     * 是否是流失玩家
     *
     * @param playerId
     * @return
     */
    public static boolean isLossPlayer(long playerId) {
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(playerId);
        if (playerRedisData == null) {
            return true;
        }
        return System.currentTimeMillis() - playerRedisData.getLastLoginDate() > LossDay * GameConstants.DAY;
    }

    /**
     * 判斷是否跳过排行奖励
     *
     * @param playerId
     * @param rankType
     * @return
     */
    public static boolean isSkipRankReward(long playerId, RankType rankType) {
        if (rankType == RankType.Arena) {
            return isLossPlayer(playerId);
        }
        return false;
    }
}
