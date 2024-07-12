package com.hm.config;

/**
 * 游戏静态参数
 *
 * @Author: siyunlong
 * @Version: V1.00
 * @Create Date: 2015-5-8下午3:22:37
 */
public class GameConstants {

    public static final byte TRUE = 1;
    public static final byte FALSE = 0;
    public static final long MILLISECOND = 1l;
    public static final long SECOND = 1000 * MILLISECOND;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    public static final long FilterLimit = 5;
    //记录玩家最近发言时间最大条数
    public static final int CHAT_TIME_RECORD_MAX = 5;

    // serverId
    public static final String RoomWorld = "room_{}_world";
    // serverId - guildId
    public static final String Guild = "room_{}_{}_guild";
    // serverId - campId
    public static final String RoomCamp = "room_{}_{}_camp";
    public static final String RoomSys = "room_{}_sys";
    public static final String RoomArea = "room_{}_{}_{}_area";

    public static final String HornName = "horn_chat";
    public static final String DBAreaName = "room_{}_area";
    public static final String DBCampName = "room_{}_camp";
    public static final String DBCampSysName = "room_{}_sys";
    public static final String DBGuildName = "room_{}_guild";

    public static final int ChatMaxSize = 80;


    public static final int[] CAMP_IDS = new int[]{1, 2, 3};

    public static final String SPLIT1 = ":"; // 1级分隔符
}
