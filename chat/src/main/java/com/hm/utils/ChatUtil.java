package com.hm.utils;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/6/30 17:16
 */
public class ChatUtil {
    public static long startTime;
    public static long endTime;

    public static boolean isNoChat(){
        if(startTime<=0&&endTime<=0){
            return false;
        }
        long now = System.currentTimeMillis();
        return now>=startTime&&now<=endTime;
    }

    public static void setNoChatTime(long startTime,long endTime){
        ChatUtil.startTime = startTime;
        ChatUtil.endTime = endTime;
    }
    public static void setStartTime(long time){
        ChatUtil.startTime = time;
    }
    public static void setEndTime(long time){
        ChatUtil.endTime = time;
    }
}
