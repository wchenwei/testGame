package com.hm.message;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2021/1/18 21:18
 */
public class KFScoreMessageComm implements IMessageComm {
    //===================跨服竞技=========================
    //初始化
    public static final int C2S_InitKf = 3400001;
    public static final int S2C_InitKf = 3400002;

    //跨服玩家信息
    public static final int S2C_KfPlayer = 3400003;

    //玩家部队列表信息
    public static final int S2C_PlayerWorldTroops = 3400004;

    //创建部队 - 参考游戏服务器
    public static final int C2S_CreateWorldTroop = 3400005;
    public static final int S2C_CreateWorldTroop = 3400006;
    //编辑部队 - 参考游戏服务器
    public static final int C2S_EditorWorldTroop = 3400007;
    public static final int S2C_EditorWorldTroop = 3400008;
    //解散部队 - 参考游戏服务器
    public static final int C2S_DisbandWorldTroop = 3400009;
    public static final int S2C_DisbandWorldTroop = 3400010;
    //服务器发送解散信息 - 参考游戏服务器
    public static final int S2C_UpdateWorldTroop = 3400011;

    //进入城市  - 参考游戏服务器
    public static final int C2S_AirborneTroop = 3400012;//空降

    public static final int S2C_BroadWorldCityTroopChange = 3400014;
    //城池状态更新
    public static final int WorldCityUpdate = 3400015;
    //城市详细信息
    public static final int C2S_CityDetail = 3400016;
    public static final int S2C_CityDetail = 3400017;

    public static final int S2C_CampCommand_Update = 3400025;
    public static final int S2C_CampCommand_Create = 3400026;
    public static final int S2C_CampCommand_Del = 3400027;

    //进入世界
    public static final int C2S_IntoWorld = 3400028;
    public static final int S2C_IntoWorld = 3400029;
    //城市内战斗 广播给城市内的所有人
    public static final int S2C_WorldCityFightStart = 3400030;
    //获取城市内的战斗 int cityId;
    public static final int C2S_WorldCityFightData = 3400031;
    public static final int S2C_WorldCityFightData = 3400032;
    //城市内玩家部队列表
    public static final int C2S_WorldCityTroopList = 3400033;
    public static final int S2C_WorldCityTroopList = 3400034;
    //城镇战斗最后结果
    public static final int S2C_BroadWorldCityFightResulte = 3400035;
    //获取战斗当前帧
    public static final int C2S_WorldCityFightCurFrame = 3400036;
    public static final int S2C_WorldCityFightCurFrame = 3400037;
    //离开战斗城市
    public static final int C2S_LeaveCity = 3400038;
    //玩家进入城战提示
    public static final int C2S_PlayerIntoCity = 3400039;

    public static final int S2C_BerLinCenterNpc = 3400040;

    public static final int C2S_RewardCloneTroopExp = 3400041;
    public static final int S2C_RewardCloneTroopExp = 3400042;

    //瞬间满血
    public static final int C2S_HandTroopFullHp = 3400048;//瞬间满血
    public static final int S2C_HandTroopFullHp = 3400049;
    //参数 int techId; int cityId;
    public static final int C2S_Guild_UseTactics = 3400050;//使用战术
    public static final int S2C_Guild_TacticsUpdate = 3400052;//当前军团战术列表

    //镜像部队
    public static final int C2S_CloneTroop = 3400053;
    public static final int S2C_CloneTroop = 3400054;

    public static final int C2S_Player_Vo = 3400055;//改变icon
    public static final int S2C_Player_Vo = 3400056;//玩家改名返回

    public static final int C2S_Troop_PvpOneByOne = 3400059;//单挑
    public static final int S2C_Troop_PvpOneByOne = 3400060;
    //获取单挑战斗数据
    public static final int C2S_Pvp1v1FightData = 3400061;
    public static final int S2C_Pvp1v1FightData = 3400062;

    //迁移部队和存油
    public static final int C2S_FirstKf = 3400065;
    public static final int S2C_FirstKf = 3400066;


    public static final int C2S_KFChatMsg = 3400067;


    public static final int S2C_WorldTroopMove = 3400071;
    public static final int C2S_DispatchWorldTroop = 3400072;//派遣
    public static final int C2S_AdvanceWorldTroop = 3400073;//突进
    public static final int S2C_AdvanceWorldTroop = 3400074;
    public static final int C2S_RetreatWorldTroop = 3400075;//撤退
    public static final int S2C_RetreatWorldTroop = 3400076;

    public static final int C2S_Troop_StropMove = 3400079;//停止移动
    public static final int S2C_Troop_StropMove = 3400080;
    public static final int C2S_Troop_ChangeWay = 3400081;//改变路线
    public static final int S2C_AdvanceFail =  3400083;//突进失败，人数不足

    public static final int C2S_LeaveKf = 3400084;

    public static final int C2S_KfScoreDetail = 3400085;
    public static final int S2C_KfScoreDetail = 3400086;

    public static final int C2S_KFHonorReward = 3400087;
    public static final int S2C_KFHonorReward = 3400088;

    public static final int S2C_KFGNpcOut = 3400089;//野蛮人出现

    public static final int S2C_KFEnd = 3400090;//跨服结束

    public static final int C2S_AdvanceCityTroop = 3400091;//可以突进的城池
    public static final int S2C_AdvanceCityTroop = 3400092;

}
