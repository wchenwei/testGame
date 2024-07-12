package com.hm.action.kf.kfworldwar.sum;

/**
 * 存储当前赛季信息
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/5/26 9:59
 */
public class KfWorldWarSumUtils {
    public static KfWorldWarSnumTime worldWarSnumTime = KfWorldWarSnumTime.getSnumTime();//当前赛季信息

    public static void reloadSnum() {
        worldWarSnumTime = KfWorldWarSnumTime.getSnumTime();
    }

    public static int getSnumId() {
        return getCurSnum().getId();
    }

    public static KfWorldWarSnumTime getCurSnum() {
        if (worldWarSnumTime == null || !worldWarSnumTime.isFitTime()) {
            reloadSnum();
        }
        return worldWarSnumTime;
    }

}
