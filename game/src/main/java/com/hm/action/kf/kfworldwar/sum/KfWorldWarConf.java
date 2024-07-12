package com.hm.action.kf.kfworldwar.sum;

import com.hm.action.kf.kfworldwar.WorldWarGroupCache;
import com.hm.config.GameConstants;

/**
 * TODO
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/27 15:10
 */
public class KfWorldWarConf {
    public static final int SnumMonth = 3;

    public static final int MinOpenDay = 90;
    public static final long ReadyTime = 12 * GameConstants.HOUR;

    /**
     * 初始化跨服世界大战相关数据
     */
    public static void init() {
        if (KfWorldWarSumUtils.getCurSnum() != null) {
            KfWorldWarServerTeamUtils.reloadTeamMap();
            WorldWarGroupCache.init();
        }
    }
}
