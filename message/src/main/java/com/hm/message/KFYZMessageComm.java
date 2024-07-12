package com.hm.message;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2021/1/18 21:18
 */
public class KFYZMessageComm implements IMessageComm {
    //===================跨服竞技=========================
    //初始化
    public static final int C2S_InitKf = 3300001;
    public static final int S2C_InitKf = 3300002;

    //跨服玩家信息
    public static final int S2C_KfPlayer = 3300003;

    //玩家部队列表信息
    public static final int S2C_PlayerWorldTroops = 3300004;

    //创建部队 - 参考游戏服务器
    public static final int C2S_CreateWorldTroop = 3300005;
    public static final int S2C_CreateWorldTroop = 3300006;
    //编辑部队 - 参考游戏服务器
    public static final int C2S_EditorWorldTroop = 3300007;
    public static final int S2C_EditorWorldTroop = 3300008;
    //解散部队 - 参考游戏服务器
    public static final int C2S_DisbandWorldTroop = 3300009;
    public static final int S2C_DisbandWorldTroop = 3300010;
    //服务器发送解散信息 - 参考游戏服务器
    public static final int S2C_UpdateWorldTroop = 3300011;

    //进入城市  - 参考游戏服务器
    public static final int C2S_AirborneTroop = 3300012;//空降
    public static final int S2C_AirborneTroop = 3300013;

    public static final int S2C_BroadWorldCityTroopChange = 3300014;
    //城池状态更新
    public static final int WorldCityUpdate = 3300015;
    //城市详细信息
    public static final int C2S_CityDetail = 3300016;
    public static final int S2C_CityDetail = 3300017;

    //跨服远征结束消息
    public static final int S2C_KFEnd = 3300018;

    //存石油
    public static final int C2S_SaveOil = 3300019;
    public static final int S2C_SaveOil = 3300020;

    //攻击方设立主城
    public static final int C2S_SetMainCity = 3300021;
    public static final int S2C_SetMainCity = 3300022;

    //领取城池奖励
    public static final int C2S_RewardCity = 3300023;//
    public static final int S2C_RewardCity = 3300024;//


    public static final int S2C_CampCommand_Update = 3300025;
    public static final int S2C_CampCommand_Create = 3300026;
    public static final int S2C_CampCommand_Del = 3300027;

    //进入世界
    public static final int C2S_IntoWorld = 3300028;
    public static final int S2C_IntoWorld = 3300029;
    //城市内战斗 广播给城市内的所有人
    public static final int S2C_WorldCityFightStart = 3300030;
    //获取城市内的战斗 int cityId;
    public static final int C2S_WorldCityFightData = 3300031;
    public static final int S2C_WorldCityFightData = 3300032;
    //城市内玩家部队列表
    public static final int C2S_WorldCityTroopList = 3300033;
    public static final int S2C_WorldCityTroopList = 3300034;
    //城镇战斗最后结果
    public static final int S2C_BroadWorldCityFightResulte = 3300035;
    //获取战斗当前帧
    public static final int C2S_WorldCityFightCurFrame = 3300036;
    public static final int S2C_WorldCityFightCurFrame = 3300037;
    //离开战斗城市
    public static final int C2S_LeaveCity = 3300038;
    //玩家进入城战提示
    public static final int C2S_PlayerIntoCity = 3300039;

    public static final int S2C_BerLinCenterNpc = 3300040;

    public static final int C2S_RewardCloneTroopExp = 3300041;
    public static final int S2C_RewardCloneTroopExp = 3300042;
    public static final int C2S_Relief_Troops_Pre = 3300043;
    public static final int S2C_Relief_Troops_Pre = 3300044;
    public static final int C2S_Relief_Troops = 3300045;
    public static final int S2C_Relief_Troops = 3300046;
    public static final int S2C_Relief_Info = 3300047;
    //瞬间满血
    public static final int C2S_HandTroopFullHp = 3300048;//瞬间满血
    public static final int S2C_HandTroopFullHp = 3300049;
    //参数 int techId; int cityId;
    public static final int C2S_Guild_UseTactics = 3300050;//使用战术
    public static final int S2C_Guild_UseTactics = 3300051;//使用战术
    public static final int S2C_Guild_TacticsUpdate = 3300052;//当前军团战术列表

    //镜像部队
    public static final int C2S_CloneTroop = 3300053;
    public static final int S2C_CloneTroop = 3300054;

    public static final int C2S_Player_Vo = 3300055;//改变icon
    public static final int S2C_Player_Vo = 3300056;//玩家改名返回
    //玩家排行
    public static final int C2S_GameRank = 3300057;
    public static final int S2C_GameRank = 3300058;

    public static final int C2S_Troop_PvpOneByOne = 3300059;//单挑
    public static final int S2C_Troop_PvpOneByOne = 3300060;
    //获取单挑战斗数据
    public static final int C2S_Pvp1v1FightData = 3300061;
    public static final int S2C_Pvp1v1FightData = 3300062;
    //战斗开始购买石油
    public static final int C2S_Treasury_Collection = 3300063;//府库征收
    public static final int S2C_Treasury_Collection = 3300064;//府库征收

    //迁移部队和存油
    public static final int C2S_FirstKf = 3300065;
    public static final int S2C_FirstKf = 3300066;


    public static final int C2S_KFChatMsg = 3300067;
    public static final int S2C_KFChatMsg = 3300068;
    public static final int S2C_KFChatMsgList = 3300069;


    public static final int S2C_WorldTroopRecoverHp = 3300070;
    public static final int S2C_WorldTroopMove = 3300071;
    public static final int C2S_DispatchWorldTroop = 3300072;//派遣
    public static final int C2S_AdvanceWorldTroop = 3300073;//突进
    public static final int S2C_AdvanceWorldTroop = 3300074;
    public static final int C2S_RetreatWorldTroop = 3300075;//撤退
    public static final int S2C_RetreatWorldTroop = 3300076;

    public static final int C2S_Troop_StropMove = 3300079;//停止移动
    public static final int S2C_Troop_StropMove = 3300080;
    public static final int C2S_Troop_ChangeWay = 3300081;//改变路线
    public static final int S2C_Troop_ChangeWay = 3300082;
    public static final int S2C_AdvanceFail =  3300083;//突进失败，人数不足

    public static final int C2S_LeaveKf = 3300084;

}
