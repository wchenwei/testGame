package com.hm.message;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2021/1/18 21:18
 */
public class KFRankMessageComm implements IMessageComm {
    //===================跨服=========================
    public static final int C2S_OpenKf = 3100001;
    public static final int S2C_OpenKf = 3100002;

    //跨服玩家信息
    public static final int S2C_KfPlayer = 3100003;


    //匹配
    public static final int C2S_LevelMatch = 3100005;
    public static final int S2C_LevelMatch = 3100006;

    //获取对手信息
    public static final int C2S_getOppInfo = 3100007;
    public static final int S2C_getOppInfo = 3100008;

    //开始战斗
    public static final int C2S_LevelFight = 3100009;
    public static final int S2C_LevelFight = 3100010;

    //获取战斗记录
    public static final int C2S_KfRecord = 3100011;
    public static final int S2C_KfRecord = 3100012;

    //购买次数
    public static final int C2S_BuyCount = 3100013;
    public static final int S2C_BuyCount = 3100014;

    public static final int C2S_LevelFightRevenge = 3100015;
    public static final int S2C_LevelFightRevenge = 3100016;


    public static final int C2S_Kings = 3100017;
    public static final int S2C_Kings = 3100018;

    public static final int S2C_ZeroMsg = 3100019;
    //获取玩家详细信息
    public static final int C2S_Player_Vo = 3100020;//改变icon
    public static final int S2C_Player_Vo = 3100021;//玩家改名返回

    //玩家排行
    public static final int C2S_GameRank = 3100022;
    public static final int S2C_GameRank = 3100023;

    public static final int C2S_LeaveKf = 3100024;
}
