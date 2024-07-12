package com.hm.mq.redis;

/**
 * 排行log
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/8/1 17:09
 */
public class RedisRankLog {
    public static boolean isShowLog = false;

    public static void showLog(String info) {
        if (isShowLog) {
            System.err.println(info);
        }
    }
}
