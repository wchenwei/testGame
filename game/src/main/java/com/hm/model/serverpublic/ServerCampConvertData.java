package com.hm.model.serverpublic;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import lombok.Getter;

import java.util.List;

public class ServerCampConvertData extends ServerPublicContext {
    /**
     * 玩法开启时间（开服时间、功能上线时间）取当天00:00:00
     */
    private long startTime;

    @Getter
    private String mask;

    /**
     * 转入最强阵营需要消耗金砖
     * camp id, 积分由大到小
     */
    @Getter
    private List<Integer> campRank = Lists.newArrayList(1, 2, 3);

    public boolean initStartTime(int serverId) {
        if (startTime > 0) {
            return false;
        }
        // 开始时间设置为当天00:00:00
        startTime = DateUtil.getNowDay(System.currentTimeMillis());
        mask = buildMask(serverId);
        SetChanged();
        return true;
    }

    public long getEndTime() {
        int num = getRoundNum();
        return startTime + (7 * GameConstants.DAY * (num + 1));
    }

    public long getOpenEndTime() {
        int num = getRoundNum();
        // 首轮没开放时间
        if (num <= 0) {
            return 0L;
        }
        return startTime + (7 * GameConstants.DAY * num) + 2 * GameConstants.DAY;
    }

    // 1.2.3.4.5.6.7 --里面的3，第三天0点清理累计积分 (除了第一周)
    public boolean canClearScore() {
        long sub = System.currentTimeMillis() - startTime;
        long r = (sub / GameConstants.DAY) % 7;
        return sub > 3 * GameConstants.DAY && r == 2;
    }

    /**
     * 是否开放日(每轮7天，每轮的前2天)
     *
     * @return
     */
    public boolean isInOpenTime() {
        long sub = System.currentTimeMillis() - startTime;
        long r = sub % (7 * GameConstants.DAY);
        return sub > 2 * GameConstants.DAY && r < 2 * GameConstants.DAY;
    }

    /**
     * 当前轮数
     *
     * @return
     */
    public int getRoundNum() {
        long sub = System.currentTimeMillis() - startTime;
        return Math.max(0, (int) (sub / (7 * GameConstants.DAY)));
    }

    private String buildMask(int serverId) {
        return String.format("%d_%d", serverId, getRoundNum());
    }

    /**
     * 是否需要开启新一轮
     *
     * @param serverId
     * @return
     */
    public boolean canOpenNewRound(int serverId) {
        return !StrUtil.equals(mask, buildMask(serverId));
    }

}
