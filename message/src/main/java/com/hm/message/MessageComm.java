package com.hm.message;

/**
 * 消息区间 0->1000000
 * @author 司云龙
 * @version 1.0
 * @date 2021/1/18 21:18
 */
public class MessageComm implements IMessageComm {
    //===================通用消息801 - 1000================================
    public static final int S2C_ErrorMsg = 101;
    public static final int S2C_ErrorMsg_Login = 102;
    public static final int G2C_SyncRpcServer = 103;

    //======================业务服务器 1001 - 2000===============================
    public static final int C2S_InitLogin = 1001;//初始化登录
    public static final int S2C_InitLogin = 1002;//初始化
    public static final int C2S_CreatePlayer = 1003;
    public static final int S2C_CreatePlayer = 1004;
    public static final int S2C_LeaveOnline = 1005;
    public static final int C2S_LoadPlayer = 1006;
    public static final int S2C_LoadPlayer = 1007;
    //==================玩家改变信息=====================
    public static final int S2C_PlayerUpdate = 1008;
    public static final int S2C_PlayerBaseInfoUpdate = 1009;
    public static final int S2C_PlayerLevelUpdate = 1010;
    public static final int S2C_LeavePlayer = 1012;

    //参数 string id;
    public static final int S2C_Recharge = 1013;//服务器发送客户端充值成功
    //此消息只用于测试，后面会干掉
    public static final int C2S_FreeRecharge_Test = 1014;//客户端请求充值
    //参数 string id;
    public static final int C2S_DayGift = 1015;//每日领取奖励-月卡奖励
    public static final int S2C_DayGift = 1016;//每日领取奖励-月卡奖励

    public static final int C2S_BuyItem = 1017;//玩家购买道具
    public static final int S2C_BuyItem = 1018;//玩家购买道具
    //玩家领取首冲礼包 无参数
    public static final int C2S_RewardFirstRmbGift = 1019;
    //请求cd
    public static final int C2S_PlayerCd = 1020;
    public static final int C2S_SellItem = 1021;//玩家卖出道具
    public static final int S2C_SellItem = 1022;//玩家卖出道具

    public static final int C2S_BuyShopVipItem = 1023;//玩家vip礼包
    public static final int S2C_BuyShopVipItem = 1024;//玩家vip礼包

    public static final int S2C_PushTimeGift = 1025;//限时礼包

    public static final int C2S_CloneLogin = 1028;//clone登录
    public static final int S2C_CloneLogin = 1029;//clone登录返回
    public static final int S2C_ShowItemReward = 1030;
    public static final int S2C_FunctionUnlock = 1031;


    //新手引导
    public static final int C2S_PlayerGuide = 1105;//新手引导

    public static final int C2S_ChangeSpeGuide = 1106;//特殊新手引导
    public static final int C2S_GuideStep = 1107;//新手引导步数
    public static final int C2S_ClientUpSdk = 1108;//客户端上传草花事件
    public static final int C2S_ClientItemNotEnough = 1109;//客户端道具不足触发

    public static final int C2S_CreateCPOrder = 1156;
    public static final int S2C_CreateCPOrder = 1157;

    public static final int S2C_Flop_Start = 2015;//开始翻牌
    public static final int C2S_Flop = 2017;//翻牌
    public static final int S2C_Flop = 2018;
    public static final int C2S_Flop_End = 2019;//结束翻牌
    public static final int S2C_Flop_End = 2020;
    public static final int C2S_Flop_All = 2021;//全部翻牌
    public static final int S2C_Flop_All = 2022;
    public static final int C2S_CityGuide_Reward = 2023;//城市章节任务奖励
    public static final int S2C_CityGuide_Reward = 2024;

    //战役
    public static final int C2S_Battle_Fight = 2030;//战斗
    public static final int S2C_Battle_Fight = 2031;
    public static final int C2S_Battle_Sweep = 2032;//扫荡
    public static final int S2C_Battle_Sweep = 2033;
    public static final int C2S_Battle_StarReward = 2034;//星级奖励
    public static final int S2C_Battle_StarReward = 2035;
    public static final int C2S_Battle_BuyCount = 2036;//购买次数
    public static final int S2C_Battle_BuyCount = 2037;
    public static final int C2S_Expedition_Fight = 2038;//远征战斗
    public static final int S2C_Expedition_Fight = 2039;
    public static final int C2S_Expedition_Reset = 2040;//远征重置
    public static final int S2C_Expedition_Reset = 2041;
    public static final int C2S_Expedition_Box = 2042;//远征领取箱子
    public static final int S2C_Expedition_Box = 2043;
    public static final int C2S_Expedition_Sweep = 2044;//远征扫荡
    public static final int S2C_Expedition_Sweep = 2045;
    public static final int C2S_Expedition_Enemy = 2046;//获取敌人信息
    public static final int S2C_Expedition_Enemy = 2047;
    public static final int C2S_Battle_Reset = 2048;//重置
    public static final int S2C_Battle_Reset = 2049;
    public static final int C2S_Tower_Sweep = 2050;//扫塔
    public static final int S2C_Tower_Sweep = 2051;
    public static final int C2S_Battle_Receive_HistoryReward = 2052;//领取历史最高奖励
    public static final int S2C_Battle_Receive_HistoryReward = 2053;
    public static final int C2S_Battle_Fight_check = 2054;//战斗校验
    public static final int C2S_Expedition_BuyBuff = 2055;//远征购买buff
    public static final int S2C_Expedition_BuyBuff = 2056;
    public static final int C2S_RecoveryBattle = 2057;//战场寻宝
    public static final int S2C_RecoveryBattle = 2058;//战场寻宝
    public static final int C2S_FightEndBattle = 2059;//血战到底
    public static final int S2C_FightEndBattle = 2060;
    public static final int C2S_FightEndReset = 2061;//血战到底-复活坦克
    public static final int S2C_FightEndReset = 2062;
    public static final int C2S_TowerBattle_Pass = 2063;//使命之路跳过战斗
    public static final int S2C_TowerBattle_Pass = 2064;
    public static final int C2S_PVE_Start = 2065;//PVE开始进入消息
    public static final int S2C_PVE_Start = 2066;//PVE开始进入消息
    public static final int C2S_FarWin_ReceiveDay = 2067;//决胜千里领取每日奖励
    public static final int S2C_FarWin_ReceiveDay = 2068;
    public static final int C2S_FarWin_MileReward = 2069;//决胜千里领取里程奖励
    public static final int S2C_FarWin_MileReward = 2070;

    //新前线战争
    public static final int C2S_FrontBattle_CreateTroop = 2071;//创建部队
    public static final int S2C_FrontBattle_CreateTroop = 2072;
    public static final int C2S_FrontBattle_Share = 2073;//分享
    public static final int S2C_FrontBattle_Share = 2074;
    public static final int C2S_FrontBattle_Join = 2075;//加入部队
    public static final int S2C_FrontBattle_Join = 2076;
    public static final int C2S_FrontBattle_Show = 2077;//显示我的部队
    public static final int S2C_FrontBattle_Show = 2078;
    public static final int C2S_FrontBattle_OpenTroop = 2079;//打开
    public static final int S2C_FrontBattle_OpenTroop = 2080;
    public static final int C2S_FrontBattle_Quit = 2081;//退出队伍
    public static final int S2C_FrontBattle_Quit = 2082;
    public static final int C2S_FrontBattle_Kick = 2083;//踢人
    public static final int S2C_FrontBattle_Kick = 2084;
    public static final int C2S_FrontBattle_ShowTroops = 2085;//显示所有部队
    public static final int S2C_FrontBattle_ShowTroops = 2086;
    public static final int C2S_FrontBattle_Fight = 2087;//战斗
    public static final int S2C_FrontBattle_Fight = 2088;
    public static final int C2S_FrontBattle_Reference = 2089;//刷新
    public static final int S2C_FrontBattle_Reference = 2090;
    public static final int S2C_FrontBattle_TroopChange = 2092;//队伍信息发生变化
    public static final int C2S_FrontBattle_ChangeTroopType = 2093;//切换部队类型
    public static final int S2C_FrontBattle_ChangeTroopType = 2094;//


    //===================等级事件====================
    public static final int C2S_LevelEvent_Fight_Start = 2100;
    public static final int C2S_LevelEvent_Fight = 2101;//等级事件战斗
    public static final int S2C_LevelEvent_Fight = 2102;
    public static final int C2S_LevelEvent_Reset = 2103;//重置等级事件
    public static final int S2C_LevelEvent_Reset = 2104;
    public static final int C2S_LevelEvent_ReceiveReward = 2105;//领取等级事件奖励
    public static final int S2C_LevelEvent_ReceiveReward = 2106;

    //新等级事件
    public static final int C2S_Event_Fight_Start = 2107;
    public static final int C2S_Event_Fight = 2109;//等级事件战斗
    public static final int S2C_Event_Fight = 2110;
    public static final int C2S_Event_Reset = 2111;//重置等级事件
    public static final int S2C_Event_Reset = 2112;
    public static final int C2S_Event_ReceiveReward = 2113;//领取等级事件奖励
    public static final int S2C_Event_ReceiveReward = 2114;
    public static final int C2S_Event_Dispatch = 2115;//派遣
    public static final int S2C_Event_Dispatch = 2116;
    public static final int C2S_Event_CallSupport = 2117;//呼叫支援
    public static final int S2C_Event_CallSupport = 2118;
    public static final int S2C_Event_BuffChange = 2119;//通知客户端buff改变

    public static final int C2S_Event_Choice = 2121;//选择事件
    public static final int S2C_Event_Choice = 2122;//选择事件
    public static final int C2S_Event_ChoiceStart = 2123;//选择起点
    public static final int S2C_Event_ChoiceStart = 2124;//选择起点


    public static final int C2S_RecoveryBattleAll = 2201;//战场一键寻宝
    public static final int S2C_RecoveryBattleAll = 2202;


    //==================玩家信息============================
    public static final int C2S_Player_BuyEnergy = 2301;//玩家购买精力
    public static final int C2S_Player_Heart = 2304;//心跳
    public static final int S2C_Player_Notice = 2310;//发送广播
    public static final int C2S_Player_Notice = 2309;//发送广播返回
    public static final int C2S_Player_Rename = 2311;//修改名字
    public static final int S2C_Player_Rename = 2313;//玩家改名返回
    public static final int S2C_Player_UpLvGift = 2315;//升级礼包
    public static final int S2C_Player_MaxCombat = 2316;//最大战力请求
    public static final int C2S_Player_MaxCombat = 2317;//最大战力返回
    public static final int C2S_Player_ServerData = 2318;//获取服务器公用数据请求
    public static final int S2C_Player_ServerData = 2319;//获取服务器公用数据返回
    public static final int C2S_Bag_UseItem = 2322;//背包使用道具
    public static final int S2C_Bag_UseItem = 2323;//打开礼包消息
    public static final int S2C_Open_Gift = 2324;//打开礼包消息
    public static final int S2C_Bag_UseItem_Suc = 2326;//使用道具成功消息
    public static final int C2S_Player_ChangeIcon = 2328;//改变icon
    public static final int S2C_Player_ChangeIcon = 2329;//玩家改名返回

    public static final int S2C_Heart = 2330;//心跳返回
    //玩家排行
    public static final int C2S_GameRank = 2332;
    public static final int S2C_GameRank = 2333;

    public static final int C2S_Player_Create_Name = 2334;//创建名字

    public static final int C2S_MyRank = 2335;
    public static final int S2C_MyRank = 2336;


    public static final int C2S_Player_Set = 2352;//玩家设置的接口
    public static final int S2C_Player_Set = 2353;

    public static final int C2S_Player_LevelTarget = 2354;//领取等级目标奖励
    public static final int S2C_Player_LevelTarget = 2355;

    public static final int S2C_Player_PartsCompose = 2356;//装备配件，合成装备

    public static final int C2S_Player_Vo = 2357;//改变icon
    public static final int S2C_Player_Vo = 2358;//玩家改名返回
    public static final int C2S_Player_ChangeFrame = 2359;//改变头像框
    public static final int S2C_Player_ChangeFrame = 2360;

    public static final int C2S_RewardHonorLine = 2361;//荣誉奖励
    public static final int S2C_RewardHonorLine = 2362;
    public static final int S2C_SendSysNotice = 2364;
    //日期变更
    public static final int S2C_DateChange = 2365;

    //更换称号
    public static final int C2S_Change_Title = 2366;//更换称号
    public static final int S2C_Change_Title = 2367;//更换称号
    public static final int C2S_Reference_Title = 2368;//刷新称号
    public static final int S2C_Reference_Title = 2369;//刷新称号
    public static final int C2S_Get_Title = 2370;//获取称号列表
    public static final int S2C_Get_Title = 2371;//获取称号列表
    public static final int C2S_Get_TitleTest = 2372;//增加称号（测试）
    public static final int S2C_Get_TitleTest = 2373;//增加称号（测试）

    public static final int C2S_ReduceDiamondTest = 2374;//扣除元宝
    public static final int S2C_ReduceDiamondTest = 2375;//扣除元宝
    public static final int C2S_IosItemTest = 2376;//扣除资源给道具
    public static final int S2C_IosItemTest = 2377;//扣除资源给道具

    public static final int C2S_WarCraft_Lvup = 2378;//兵法书升级
    public static final int S2C_WarCraft_Lvup = 2379;

    public static final int C2S_WarCraft_Skill = 2380;//兵法技能升级
    public static final int S2C_WarCraft_Skill = 2381;
    public static final int S2C_PlayerLog = 2382;//记录玩家行为

    public static final int C2S_RemovePlayer = 2383;//注销角色
    public static final int S2C_RemovePlayer = 2384;

    public static final int C2S_Player_RandomName = 2385;//随机玩家角色名
    public static final int S2C_Player_RandomName = 2386;

    public static final int C2S_Player_WXSubs = 2387;//微信订阅
    public static final int S2C_Player_WXSubs = 2388;

    public static final int C2S_Player_ShowAd = 2389;//观看广告
    public static final int S2C_Player_ShowAd = 2390;

    public static final int C2S_Player_AdReward = 2391;//观看广告
    public static final int S2C_Player_AdReward = 2392;

    public static final int C2S_QYWX_Item = 2393;
    public static final int S2C_QYWX_Item = 2394;
    public static final int C2S_Player_Share = 2395;
    public static final int S2C_Player_Share = 2396;

    public static final int C2S_Player_ShareReward = 2397;
    public static final int S2C_Player_ShareReward = 2398;

    //==================玩家副本=========================
    public static final int C2S_PlayerFb_Fight = 2401;//副本攻打
    public static final int S2C_PlayerFb_Fight = 2402;//副本攻打
    public static final int C2S_PlayerFb_StarReward = 2403;//总星奖励
    public static final int S2C_PlayerFb_StarReward = 2404;//总星奖励
    public static final int C2S_PlayerFb_BuyFightNum = 2405;//购买挑战次数
    public static final int S2C_PlayerFb_BuyFightNum = 2406;//购买挑战次数
    public static final int C2S_PlayerFb_Sweep = 2407;//副本扫荡
    public static final int S2C_PlayerFb_Sweep = 2408;
    public static final int C2S_Tank_Research = 2409;//战兽驯化
    public static final int S2C_Tank_Research = 2410;
    public static final int C2S_Tank_Test = 2411;//坦克测试
    public static final int S2C_Tank_Test = 2412;//坦克测试
    public static final int C2S_Request_Tank_Flop = 2413;//坦克测试请求翻牌
    public static final int S2C_Request_Tank_Flop = 2414;//

    public static final int C2S_Sync_GuideInfo = 2417;//同步章节信息
    public static final int S2C_Sync_GuideInfo = 2418;
    public static final int C2S_PlayerFb_Fight_Start = 2419;//副本攻打开始
    public static final int S2C_PlayerFb_Fight_Start = 2420;

    public static final int C2S_PlayerFb_Record = 2421;//副本记录
    public static final int S2C_PlayerFb_Record = 2422;

    public static final int C2S_Battle_Record = 2423;//pve首次通过记录
    public static final int S2C_Battle_Record = 2424;

    public static final int C2S_Battle_RecordReward = 2425;//pve首次通过记录奖励
    public static final int S2C_Battle_RecordReward = 2426;
    public static final int S2C_Battle_PveRecordMaxChange = 2427;

    public static final int C2S_PlayerFB_WaveEnd = 2428;
    public static final int S2C_PlayerFB_WaveEnd = 2429;

    public static final int C2S_PlayerFB_ShowBox = 2430;
    public static final int S2C_PlayerFB_ShowBox = 2431;
    public static final int C2S_PlayerFB_GetBox = 2432;
    public static final int S2C_PlayerFB_GetBox = 2433;

    public static final int C2S_PlayerFB_StopOnhook = 2434;
    public static final int S2C_PlayerFB_StopOnhook = 2435;

    public static final int C2S_PlayerFB_ShowOffLineBox = 2436;
    public static final int S2C_PlayerFB_ShowOffLineBox = 2437;
    public static final int C2S_PlayerFB_GetOffLineBox = 2438;
    public static final int S2C_PlayerFB_GetOffLineBox = 2439;

    //兑换码
    public static final int C2S_Voucher = 2500;//兑换
    public static final int S2C_Voucher = 2501;//兑换
    //
    public static final int C2S_Help = 2502;//帮助
    public static final int S2C_Need_Help = 2503;//有需要帮助的信息
    public static final int C2S_Help_Info = 2504;
    public static final int S2C_Help_Info = 2505;
    public static final int S2C_BeHelped = 2506;//发送被帮助信息
    public static final int C2S_Send_Help = 2507;//发送帮助请求
    public static final int S2C_ReportTar = 2508;//举报别人
    public static final int C2S_ReportTar = 2509;//举报别人

    public static final int S2C_CreatePlayerCostom = 2510;//创建自定义阵容
    public static final int C2S_CreatePlayerCostom = 2511;//创建自定义阵容
    public static final int S2C_RemovePlayerCostom = 2512;//删除自定义阵容
    public static final int C2S_RemovePlayerCostom = 2513;//删除自定义阵容
    public static final int C2S_ChangePlayerCostomName = 2514;//修改阵容昵称
    public static final int S2C_ChangePlayerCostomName = 2515;//修改阵容昵称

    public static final int C2S_IosUpdateReward = 2516;//ios马甲包热更奖励
    public static final int S2C_IosUpdateReward = 2517;//ios马甲包热更奖励
    //--------gm命令---------------
    public static final int C2S_Test_AddItems = 2601;//gm命令添加物品
    //TODO 提供给客户端的测试方法，后续需要删除 把任务设置成完成状态
    public static final int C2S_Test_Task_Complete = 2602;//

    public static final int C2S_Test_ChangeMissionId = 2603;//更改关卡进度
    public static final int C2S_Test_Cmd = 2604;//cl
    public static final int C2S_Test_PvpFight = 2605;//测试战斗数据
    public static final int S2C_Test_PvpFight = 2606;
    public static final int S2C_Test_Spend = 2607;

    //测试消息
    public static final int S2C_Test_Msg0 = 2610;
    public static final int S2C_Test_Msg1 = 2611;
    public static final int S2C_Test_Msg2 = 2612;
    public static final int S2C_Test_Msg3 = 2613;
    public static final int S2C_Test_Msg4 = 2614;
    public static final int S2C_Test_Msg5 = 2615;
    public static final int S2C_Test_Msg6 = 2616;
    public static final int S2C_Test_Msg7 = 2617;
    public static final int S2C_Test_Msg8 = 2618;
    public static final int S2C_Test_Msg9 = 2619;
    //商店
    public static final int C2S_Buy_Goods = 2701;//购买商品
    public static final int S2C_Buy_Goods = 2702;
    public static final int C2S_Refresh_Goods = 2703;//刷新商品
    public static final int S2C_Refresh_Goods = 2704;

    //===================玩家部队相关============================
    //玩家部队列表更新
    public static final int S2C_PlayerWorldTroops = 2801;
    //创建部队
    public static final int C2S_CreateWorldTroop = 2802;
    public static final int S2C_CreateWorldTroop = 2803;
    public static final int C2S_EditorWorldTroop = 2804;
    public static final int S2C_EditorWorldTroop = 2805;
    public static final int C2S_DisbandWorldTroop = 2806;
    public static final int S2C_DisbandWorldTroop = 2807;
    //更新一只部队
    public static final int S2C_UpdateWorldTroop = 2808;
    public static final int S2C_WorldTroopRecoverHp = 2809;
    public static final int S2C_WorldTroopMove = 2810;
    public static final int C2S_DispatchWorldTroop = 2811;//派遣
    public static final int S2C_DispatchWorldTroop = 2812;
    public static final int C2S_AdvanceWorldTroop = 2813;//突进
    public static final int S2C_AdvanceWorldTroop = 2814;
    public static final int C2S_RetreatWorldTroop = 2815;//撤退
    public static final int S2C_RetreatWorldTroop = 2816;
    public static final int C2S_HandTroopFullHp = 2817;//瞬间满血
    public static final int S2C_HandTroopFullHp = 2818;
    public static final int C2S_Troop_StropMove = 2819;//停止移动
    public static final int S2C_Troop_StropMove = 2820;
    public static final int C2S_Troop_ChangeWay = 2821;//改变路线
    public static final int S2C_Troop_ChangeWay = 2822;
    public static final int C2S_Troop_PvpOneByOne = 2823;//单挑
    public static final int S2C_Troop_PvpOneByOne = 2824;
    //参数： id
    public static final int C2S_WarResult_Record = 2825;//战斗录像
    public static final int S2C_WarResult_Record = 2826;
    //获取引导国战
    public static final int C2S_GuidWarData = 2831;
    public static final int S2C_GuidWarData = 2832;

    //战斗挂机设置
    public static final int C2S_WarHelper_Set = 2833;
    public static final int S2C_WarHelper_Set = 2834;
    //结束挂机
    public static final int C2S_WarHelper_End = 2835;
    public static final int S2C_WarHelper_End = 2836;
    //请求挂机信息
    public static final int C2S_WarHelper = 2837;
    public static final int S2C_WarHelper = 2838;
    //挂机信息发生变化
    public static final int S2C_WarHelper_Change = 2840;
    //参数 String troopId; int cityId;
    public static final int C2S_AirborneTroop = 2841;//空降
    public static final int S2C_AirborneTroop = 2842;
    public static final int S2C_AdvanceFail = 2843;//突进失败，人数不足
    //镜像部队
    public static final int C2S_CloneTroop = 2844;
    //战斗掉落
    public static final int S2C_PvpFightDrop = 2845;
    //幽灵boss出现
    public static final int S2C_GhostBossCreate = 2846;
    //领取克隆经验奖励
    public static final int C2S_RewardCloneTroopExp = 2847;
    public static final int S2C_RewardCloneTroopExp = 2848;

    public static final int S2C_CloneTroop = 2849;
    public static final int S2C_FindBlackMainCity = 2850;

    public static final int C2S_Troop_BindFormation = 2851;
    public static final int S2C_Troop_BindFormation = 2852;
    //挂机离线统计数据
    public static final int S2C_WarHelp_LeaveData = 2853;

    public static final int C2S_GiveUp_City = 2854;//放弃城池
    public static final int S2C_GiveUp_City = 2855;

    public static final int C2S_AdvanceCityTroop = 2856;//可以突进的城池
    public static final int S2C_AdvanceCityTroop = 2857;

    //==================玩家邮件=====================
    public static final int S2C_LoadMailList = 3101;
    public static final int S2C_AddMail = 3112;  //推送新邮件
    public static final int C2S_ReadMail = 3103;  //打开邮件
    public static final int S2C_ReadMail = 3104;
    public static final int C2S_GetMailReward = 3105;  //领取附件
    public static final int S2C_GetMailReward = 3106;
    public static final int C2S_ReadAllMail = 3107;  //全部已读
    public static final int S2C_ReadAllMail = 3108;
    public static final int C2S_DelAllMail = 3109; //一键删除
    public static final int S2C_DelAllMail = 3110;
    public static final int S2C_AddMailList = 3111;  //推送新邮件
    public static final int  C2S_DelOne_Mail = 3113; //删除一个邮件
    public static final int  S2C_DelOne_Mail = 3114;
    //==================聊天设置=====================
    public static final int C2S_SetBlack = 4001; //设置黑名单
    public static final int S2C_SetBlack = 4002;
    public static final int C2S_OpenBlack = 4003; //打开黑名单
    public static final int S2C_OpenBlack = 4004;
    public static final int C2S_OpenRedPacket = 4005; //打开红包
    public static final int S2C_OpenRedPacket = 4006;


    //=================世界相关=====================================
    //城市内战斗 广播给城市内的所有人
    public static final int S2C_WorldCityFightStart = 5000;

    //进入世界
    public static final int C2S_IntoWorld = 5001;
    public static final int S2C_IntoWorld = 5002;
    //离开世界
    public static final int C2S_LeaveWorld = 5003;
    public static final int S2C_LeaveWorld = 5004;

    //城池状态更新
    public static final int WorldCityUpdate = 5007;

    //获取城市内的战斗 int cityId;
    public static final int C2S_WorldCityFightData = 5011;
    public static final int S2C_WorldCityFightData = 5012;
    public static final int S2C_BroadWorldCityTroopChange = 5013;
    //城市内玩家部队列表
    public static final int C2S_WorldCityTroopList = 5014;
    public static final int S2C_WorldCityTroopList = 5015;
    //城镇战斗最后结果
    public static final int S2C_BroadWorldCityFightResulte = 5016;
    //获取战斗当前帧
    public static final int C2S_WorldCityFightCurFrame = 5017;
    public static final int S2C_WorldCityFightCurFrame = 5018;
    //离开战斗城市
    public static final int C2S_LeaveCity = 5019;
    //获取单挑战斗数据
    public static final int C2S_Pvp1v1FightData = 5020;
    public static final int S2C_Pvp1v1FightData = 5021;
    //玩家进入城战提示
    public static final int C2S_PlayerIntoCity = 5022;

    //==================礼包===============================
    public static final int C2S_GetNewPlayerGift = 5101;
    public static final int S2C_GetNewPlayerGift = 5102;

    public static final int C2S_GetGrowFund = 5103;
    public static final int S2C_GetGrowFund = 5104;

    public static final int C2S_FreeGift = 5105;
    public static final int S2C_FreeGift = 5106;

    //==================国战相关===========================
    //国战开始结束消息
    public static final int S2C_GuildWarState = 5200;



    //服务器数据变化
    public static final int S2C_ServerDataChange = 5321;
    //总统特权信息发生变化
    public static final int S2C_ServerPowerChange = 5322;
    //世界等级变化消息
    public static final int S2C_WorldLvChange = 5323;
    //阵营联盟更改
    public static final int S2C_CampAllianceChange = 5324;
    //服务器部落任务变化
    public static final int S2C_ServerGuildTaskChange = 5325;
    //服务器世界建筑变化
    public static final int S2C_ServerWorldBuildChange = 5326;
    //柏林boss攻打
    public static final int C2S_BerLinBossFight = 5327;
    public static final int S2C_BerLinBossFight = 5328;
    //广播柏林boss血量变化
    public static final int S2C_BerLinBossHpChange = 5329;

    //===================世界建筑==============================
    //颁布
    public static final int C2S_WorldBuildReleaseTask = 5401;
    public static final int S2C_WorldBuildReleaseTask = 5402;
    //领取每日建筑奖励
    public static final int C2S_WorldBuildDayReward = 5403;
    public static final int S2C_WorldBuildDayReward = 5404;
    //派遣部队采集
    public static final int C2S_WorldBuildTroopMine = 5405;
    public static final int S2C_WorldBuildTroopMine = 5406;
    //当前世界建筑部队变化
    public static final int S2C_WorldBuildTroopChange = 5407;
    //遣返部队采集
    public static final int C2S_WorldBuildTroopBack = 5408;
    public static final int S2C_WorldBuildTroopBack = 5409;
    //领取任务奖励
    public static final int C2S_WorldBuildCampReward = 5410;
    public static final int S2C_WorldBuildCampReward = 5411;

    public static final int C2S_GetWorldBuildData = 5412;
    public static final int S2C_GetWorldBuildData = 5413;
    //客户端采集满主动请求
    public static final int C2S_WorldBuildTroopMineFull = 5414;
    public static final int S2C_WorldBuildTroopMineFull = 5415;


    //================================================
    public static final int C2S_IntoKFWorld = 6001;
    public static final int C2S_KFMoveTroop = 6002;
    public static final int S2C_KFMoveTroop = 6003;

    //指挥官
    public static final int C2S_CarLvUp = 8001; //座驾升级
    public static final int S2C_CarLvUp = 8002;
    public static final int C2S_CarSkillLvUp = 8003; //座驾技能升级
    public static final int S2C_CarSkillLvUp = 8004;
    public static final int C2S_MilitaryLvUp = 8005; //军衔升级
    public static final int S2C_MilitaryLvUp = 8006;
    public static final int C2S_UnlockCarModel = 8007; //解锁幻化
    public static final int S2C_UnlockCarModel = 8008;
    public static final int C2S_SetCarIcon = 8009;//设置幻化头像
    public static final int S2C_SetCarIcon = 8010;
    public static final int C2S_SuperWeaponLvUp = 8011; //战鼓升级
    public static final int S2C_SuperWeaponLvUp = 8012;
    public static final int C2S_MilitaryProjectLvUp = 8013; //指挥官--军工升级
    public static final int S2C_MilitaryProjectLvUp = 8014;
    public static final int C2S_SuperWeaponUpgrade = 8015; //超武进阶
    public static final int S2C_SuperWeaponUpgrade = 8016;
    public static final int C2S_AutoDriveLvUp = 8017; //自动驾驶升级
    public static final int S2C_AutoDriveLvUp = 8018;
    public static final int C2S_CarModel_StarUp = 8019; //座驾皮肤升星
    public static final int S2C_CarModel_StarUp = 8020;
    public static final int C2S_AgentCenterLvUp = 8021; //特工中心升级
    public static final int S2C_AgentCenterLvUp = 8022;
    public static final int C2S_AgentDispatch = 8023; //特工派遣
    public static final int S2C_AgentDispatch = 8024;
    public static final int C2S_AgentUndispatch = 8025; //特工派遣撤下
    public static final int S2C_AgentUndispatch = 8026;
    public static final int C2S_AgentSync = 8027; //特工派遣同步特工状态
    public static final int S2C_AgentSync = 8028;
    public static final int C2S_AgentReward = 8029; //特工派遣领取奖励
    public static final int S2C_AgentReward = 8030;



    //坦克
    public static final int S2C_TankAdd = 9001;//招募坦克
    public static final int S2C_TankList = 9002; //坦克列表
    public static final int S2C_TankUpdate = 9003; //坦克更新
    public static final int C2S_TankLvUp = 9004; //坦克升级
    public static final int S2C_TankLvUp = 9005;
    public static final int C2S_TankMaxLvUp = 9006; //坦克一键升级
    public static final int S2C_TankMaxLvUp = 9007;
    public static final int C2S_TankStarUp = 9008; //坦克升星
    public static final int S2C_TankStarUp = 9009;
    public static final int C2S_TankUnlock = 9010; //坦克合成
    public static final int S2C_TankUnlock = 9011;
    public static final int C2S_TankSkillLv = 9012; //坦克技能升级
    public static final int S2C_TankSkillLv = 9013;
    public static final int C2S_TankPartsCompose = 9014; //坦克配件合成
    public static final int S2C_TankPartsCompose = 9015;
    public static final int C2S_TankPartsMaxCompose = 9016; //坦克配件一键合成
    public static final int S2C_TankPartsMaxCompose = 9017;
    public static final int C2S_TankReform = 9018; //坦克突破
    public static final int S2C_TankReform = 9019;
    public static final int C2S_TankResearchJunior = 9020; //坦克研究(普通)
    public static final int S2C_TankResearchJunior = 9021;
    public static final int C2S_TankResearchSenior = 9022; //坦克研究 十连抽
    public static final int S2C_TankResearchSenior = 9023;
    public static final int C2S_TankDraw = 9024; //坦克绘制
    public static final int S2C_TankDraw = 9025;
    public static final int C2S_TankDrawFre = 9026; //坦克绘制刷新
    public static final int S2C_TankDrawFre = 9027;
    public static final int C2S_TankDrawLock = 9028; //坦克刷新的锁定功能
    public static final int S2C_TankDrawLock = 9029;
    public static final int C2S_WarGodLvUp = 9030; //战神升级
    public static final int S2C_WarGodLvUp= 9031;
    public static final int C2S_DriverLvUp = 9032; //车长升级
    public static final int S2C_DriverLvUp = 9033;
    public static final int C2S_DriverSkillLvUp = 9034; //车长技能升级
    public static final int S2C_DriverSkillLvUp = 9035;
    public static final int C2S_FriendLvUp = 9036; //羁绊升级
    public static final int S2C_FriendLvUp = 9037;
    public static final int C2S_TankDetail = 9038; //查看坦克详情
    public static final int S2C_TankDetail = 9039;
    public static final int C2S_TankTechLv = 9040; //坦克科技升级
    public static final int S2C_TankTechLv = 9041;
    public static final int C2S_TarTankMsg = 9042; //获取目标用户的坦克信息
    public static final int S2C_TarTankMsg = 9043;
    public static final int C2S_EvolveStar = 9044; //兽魂进化星级，高级星级
    public static final int S2C_EvolveStar = 9045;
    public static final int C2S_SpecialLvup = 9046; //坦克--专精--升级
    public static final int S2C_SpecialLvup = 9047;
    public static final int C2S_SpecialChange = 9048; //坦克--专精--切换
    public static final int S2C_SpecialChange = 9049;
    public static final int C2S_TankRecast = 9050; //坦克--重铸
    public static final int S2C_TankRecast = 9051;
    public static final int C2S_TankDrawBan = 9052; //坦克绘制锁品阶
    public static final int S2C_TankDrawBan = 9053;
    public static final int C2S_TankSkillLvAll = 9054; //坦克技能一键升级
    public static final int S2C_TankSkillLvAll = 9055;
    public static final int C2S_TarTank = 9056; //获取目标坦克信息
    public static final int S2C_TarTank = 9057;
    public static final int C2S_TankResearchWish = 9058; //十连抽设置心愿单
    public static final int S2C_TankResearchWish = 9059;
    public static final int C2S_TankResearchBuyItem = 9060; //十连抽购买道具
    public static final int S2C_TankResearchBuyItem = 9061;
    public static final int C2S_DriverEvolveLvUp = 9062; //兽魂觉醒
    public static final int S2C_DriverEvolveLvUp = 9063;

    //竞技场
    public static final int C2S_ArenaOpen = 9101; //打开竞技场
    public static final int S2C_ArenaOpen = 9102;
    public static final int C2S_ArenaFight = 9105; //竞技场对战
    public static final int S2C_ArenaFight = 9106;
    public static final int C2S_ArenaRefresh = 9107; //刷新对手
    public static final int S2C_ArenaRefresh = 9108;
    public static final int C2S_ArenaSetTroop = 9109; //设置阵容
    public static final int S2C_ArenaSetTroop = 9110;
    public static final int C2S_ArenaRecordList = 9111; //战报列表
    public static final int S2C_ArenaRecordList = 9112;
    public static final int C2S_ArenaReplayRecord = 9113; //回放战报(不用了)
    public static final int S2C_ArenaReplayRecord = 9114;
    public static final int C2S_ArenaBuyTimes = 9115; //购买挑战次数
    public static final int S2C_ArenaBuyTimes = 9116;
    public static final int C2S_ArenaCheckFight = 9117; //检查能否战斗
    public static final int S2C_ArenaCheckFight = 9118;
    public static final int C2S_ArenaWinTimeReward = 9119;
    public static final int S2C_ArenaWinTimeReward = 9120;
    public static final int C2S_ArenaOpenForAdvanced = 9121;
    public static final int S2C_ArenaOpenForAdvanced = 9122;
    public static final int C2S_ArenaGetWorshipInfo = 9123;
    public static final int S2C_ArenaGetWorshipInfo = 9124;
    public static final int C2S_ArenaWorship = 9125;
    public static final int S2C_ArenaWorship = 9126;
    public static final int C2S_ArenaAdvancedFight = 9127;
    public static final int S2C_ArenaAdvancedFight = 9128;
    public static final int C2S_ArenaTrumpReset = 9129;
    public static final int S2C_ArenaTrumpReset = 9130;
    public static final int C2S_ArenaTrumpQuick = 9131;
    public static final int S2C_ArenaTrumpQuick = 9132;
    public static final int C2S_ArenaPrimaryQuick = 9134;
    public static final int S2C_ArenaPrimaryQuick = 9135;

    public static final int C2S_ActivityList = 9601;
    //活动列表
    public static final int S2C_ActivityList = 9602;
    //购买成长计划
    public static final int C2S_BuyGrowUpActivity = 9603;
    //领取活动奖励
    public static final int C2S_ActivityReward = 9604;
    public static final int S2C_ActivityReward = 9605;
    //累计签到奖励
    public static final int C2S_ActivitySignTotalReward = 9607;
    public static final int S2C_ActivitySignTotalReward = 9608;
    //在线抽奖活动开启
    public static final int S2C_ActivityCircleOpen = 9609;
    //三日活动购买
    public static final int C2S_ActivityThreeDayBuy = 9611;
    public static final int S2C_ActivityThreeDayBuy = 9612;

    // SS坦克活动
    public static final int C2S_ActivitySSTankSingle = 9613;
    public static final int S2C_ActivitySSTankSingle = 9614;
    public static final int C2S_ActivitySSTankTen = 9615;
    public static final int S2C_ActivitySSTankTen = 9616;
    public static final int C2S_ActivitySSTankAward = 9617;
    public static final int S2C_ActivitySSTankAward = 9618;
    public static final int C2S_ActivityTreasureExchange = 9619;
    public static final int S2C_ActivityTreasureExchange = 9620;
    // SS坦克活动
    //坦克打靶
    public static final int C2S_ActivityShooting = 9621; //坦克打靶
    public static final int S2C_ActivityShooting = 9622;

    //大富翁
    public static final int C2S_Activity_RichMan_Dice = 9623;//掷点
    public static final int S2C_Activity_RichMan_Dice = 9624;
    public static final int C2S_Activity_RichMan_Vow = 9625;//祭祀
    public static final int S2C_Activity_RichMan_Vow = 9626;
    //荣誉部落排行
    public static final int C2S_Activity_GuildHonorRank = 9627;
    public static final int S2C_Activity_GuildHonorRank = 9628;
    //首日PVE
    public static final int C2S_ClientTask = 9629;
    public static final int S2C_ClientTask = 9630;

    //81活动
    public static final int C2S_Activity_ArmyDay = 9631;	//81活动开始，探索--随机资源，第一次刷新
    public static final int S2C_Activity_ArmyDay = 9632;
    public static final int C2S_Activity_ArmyDayCost = 9633;//81活动，探索--加速消耗
    public static final int S2C_Activity_ArmyDayCost = 9634;
    public static final int C2S_Activity_ArmyDayRef = 9635;//81活动，探索--刷新
    public static final int S2C_Activity_ArmyDayRef = 9636;
    public static final int C2S_Activity_ArmyDayStart = 9637;//81活动，探索--开始挖
    public static final int S2C_Activity_ArmyDayStart = 9638;
    public static final int C2S_Activity_DevReward = 9639;//81活动，探索--领取奖励
    public static final int S2C_Activity_DevReward = 9640;
    public static final int C2S_Activity_BossHp = 9641;//81活动，boss--血量广播
    public static final int S2C_Activity_BossHp = 9642;
    public static final int C2S_Activity_BossDouble = 9643;//81活动，boss--鼓舞，增加战力百分比
    public static final int S2C_Activity_BossDouble = 9644;
    public static final int C2S_Activity_BossAdd = 9647;//81活动，boss--加入
    public static final int S2C_Activity_BossAdd = 9648;
    public static final int C2S_Activity_BossFight = 9649;//81活动，boss--加入开始打
    public static final int S2C_Activity_BossFight= 9650;
    public static final int C2S_Activity_BossRemove = 9651;//81活动，boss--关闭界面，移除
    public static final int S2C_Activity_BossRemove = 9652;
    public static final int C2S_Activity_BossNotice = 9655;//81活动，boss--弹幕
    public static final int S2C_Activity_BossNotice = 9656;
    public static final int C2S_Activity_Log = 9657;//81活动，挖出奖励日志
    public static final int S2C_Activity_Log = 9658;
    public static final int C2S_Activity_AllBossHp = 9659;//81活动，广播所有用户的血量
    public static final int S2C_Activity_AllBossHp = 9660;
    public static final int S2C_Activity_OneUpdate = 9661;//单一活动变化更新

    public static final int C2S_Activity_MergeMoonCake = 9663;//中秋节活动--合成月饼
    public static final int S2C_Activity_MergeMoonCake = 9664;
    public static final int C2S_Activity_Resource = 9665;//中秋节活动--领取材料礼盒（统计金砖消耗 获取）
    public static final int S2C_Activity_Resource = 9666;
    public static final int C2S_Activity_BuyGift = 9667;//中秋节活动--中秋好礼（金砖购买）
    public static final int S2C_Activity_BuyGift = 9668;
    public static final int C2S_Activity_BuyTimeGift = 9669;//中秋节活动-- 那兔送福（金砖购买）
    public static final int S2C_Activity_BuyTimeGift = 9610;

    //参数
    public static final int C2S_OpenJzEggs = 9670;//砸局座的蛋
    public static final int S2C_OpenJzEggs = 9671;
    public static final int C2S_JzEggsAddr = 9672;//实物地址
    public static final int S2C_JzEggsAddr = 9673;
    //参数
    public static final int C2S_JzEggsRecord = 9674;//获取获奖记录
    public static final int S2C_JzEggsRecord = 9675;

    //三日活动-新7日活动
    public static final int C2S_Activity_ThreeDay_Receive = 9681;//领取积分奖励
    public static final int S2C_Activity_ThreeDay_Receive = 9682;
    public static final int C2S_Activity_ThreeDay_Task_Reward = 9683;//领取任务奖励或商品
    public static final int S2C_Activity_ThreeDay_Task_Reward = 9684;

    //七日活动领取目标奖励
    public static final int C2S_Activity_SevenDay_Receive = 9685;
    public static final int S2C_Activity_SevenDay_Receive = 9686;

    // 每日任务开箱子
    public static final int C2S_Daily_Task_Box = 9800;
    public static final int S2C_Daily_Task_Box = 9801;
    // 每日任务 领取任务奖励
    public static final int C2S_Daily_Task_Reward = 9802;
    public static final int S2C_Daily_Task_Reward = 9803;
    //任务
    public static final int C2S_Task_Reward = 9804;//领取任务奖励
    public static final int S2C_Task_Reward = 9805;//领取任务奖励
    // 每日任务周积分奖励
    public static final int C2S_Daily_Task_Week_Reward = 9806;
    public static final int S2C_Daily_Task_Week_Reward = 9807;
    // 每日任务周活动计费点购买后刷新
    public static final int S2C_Daily_Task_Week_Refresh = 9809;

    public static final int C2S_Task_Reward_All = 9810;//领取任务奖励
    public static final int S2C_Task_Reward_All = 9811;//领取任务奖励

    public static final int C2S_Main_Task_Reward = 9812;// 主线任务领奖励
    public static final int S2C_Main_Task_Reward = 9813;


    public static final int S2C_Player_Tips = 10019; //玩家提示通知
    public static final int C2S_Player_Event_Statistic = 10020; //玩家事件统计
    public static final int S2C_Player_Event_Statistic = 10021;

    //====================补给掠夺===================================
    public static final int C2S_SupplyRefresh = 10101; //补给列表刷新
    public static final int S2C_SupplyRefresh = 10102;
    public static final int C2S_SupplyTroopStart = 10103; //开始补给
    public static final int S2C_SupplyTroopStart = 10104;
    public static final int C2S_SupplyReward = 10105; //领取补给奖励
    public static final int S2C_SupplyReward = 10106;
    public static final int C2S_OpenSupply = 10107;//打开补给掠夺界面
    public static final int S2C_OpenSupply = 10108;
    public static final int C2S_SupplyRefrshEnemy = 10109;//刷新情报
    public static final int S2C_SupplyRefrshEnemy = 10110;
    public static final int C2S_SupplyRob = 10111;//打劫
    public static final int S2C_SupplyRob = 10112;
    public static final int C2S_SupplyRecord = 10113;//记录
    public static final int S2C_SupplyRecord = 10114;

    //====================补给掠夺===================================
    //========================================
    public static final int C2S_Treasury_Collection = 20001;//府库征收
    public static final int S2C_Treasury_Collection = 20002;//府库征收
    public static final int C2S_Player_UnlockIcon = 20003;//解锁icon
    public static final int S2C_Player_UnlockIcon = 20004;

    //部落相关
    public static final int C2S_Guild_Create = 30001;//创建部落
    public static final int S2C_Guild_Create = 30002;
    public static final int C2S_Guild_Join = 30003;//申请加入部落
    public static final int S2C_Guild_Join = 30004;
    public static final int C2S_Guild_Cancel = 30005;//玩家取消加入部落申请
    public static final int S2C_Guild_Cancel = 30006;
    public static final int C2S_Guild_Refuse = 30007;//部落拒绝申请，一键拒绝所有的
    public static final int S2C_Guild_Refuse = 30008;
    public static final int C2S_Guild_Agree = 30009;//同意加入部落
    public static final int S2C_Guild_Agree = 30010;
    public static final int C2S_Guild_Get = 30011;//获取自己所在部落信息
    public static final int S2C_Guild_Get = 30012;
    public static final int C2S_Guild_Quit = 30013;//自己退出部落
    public static final int S2C_Guild_Quit = 30014;
    public static final int C2S_Guild_Trans = 30015;//转让部落
    public static final int S2C_Guild_Trans = 30016;
    public static final int C2S_Guild_Kick = 30017;//踢出成员
    public static final int S2C_Guild_Kick = 30018;
    public static final int C2S_Guild_Job = 30019;//修改成员的官职
    public static final int S2C_Guild_Job = 30020;
    public static final int C2S_Guild_ChangeName = 30021;//修改部落名字
    public static final int S2C_Guild_ChangeName = 30022;
    public static final int C2S_Guild_List = 30025;//部落列表信息
    public static final int S2C_Guild_List = 30026;
    public static final int C2S_Guild_ChangeNotice = 30027;//修改部落的公告信息
    public static final int S2C_Guild_ChangeNotice = 30028;
    public static final int C2S_Guild_GetReq = 30029;//获取部落的申请列表
    public static final int S2C_Guild_GetReq = 30030;
    public static final int C2S_Guild_PlayerChange = 30031;//部落成员变化的消息
    public static final int S2C_Guild_PlayerChange = 30032;
    public static final int C2S_Guild_PlayerGuildVo = 30033;//用户的部落基本信息
    public static final int S2C_Guild_PlayerGuildVo = 30034;
    public static final int C2S_Guild_Invite = 30035;//部落邀请用户加入
    public static final int S2C_Guild_Invite = 30036;
    public static final int C2S_Guild_WorldReward = 30037;//部落城池产出领取
    public static final int S2C_Guild_WorldReward = 30038;
    public static final int C2S_Guild_PlayerAgree = 30039;//同意加入部落
    public static final int S2C_Guild_PlayerAgree = 30040;
    public static final int C2S_Guild_PlayerRefuse = 30041;//拒绝加入部落
    public static final int S2C_Guild_PlayerRefuse = 30042;
    public static final int C2S_Guild_MoveTimes = 30043;//部落迁都次数
    public static final int S2C_Guild_MoveTimes = 30044;
    public static final int C2S_Guild_PlayerAdd = 30045;//部落有用户申请加入时，发送给部落团长副团长，用于红点提示
    public static final int S2C_Guild_PlayerAdd = 30046;
    public static final int C2S_Guild_Donation = 30047;//部落捐献
    public static final int S2C_Guild_Donation = 30048;//部落捐献
    public static final int C2S_Guild_TecUpdate = 30049;//部落科技升级
    public static final int S2C_Guild_TecUpdate = 30050;//部落科技升级
    public static final int C2S_Guild_TecReset = 30051;//部落科技重置
    public static final int S2C_Guild_TecReset = 30052;//部落科技重置
    //参数 int techId; int cityId;
    public static final int C2S_Guild_UseTactics = 30053;//使用战术
    public static final int S2C_Guild_UseTactics = 30054;//使用战术
    public static final int S2C_Guild_TacticsUpdate = 30055;//当前部落战术列表
    public static final int C2S_Guild_ChangeFlag = 30056;//部落修改旗帜
    public static final int S2C_Guild_ChangeFlag = 30057;
    public static final int C2S_Guild_RefusePlayer = 30058;//部落拒绝单个玩家申请
    public static final int S2C_Guild_RefusePlayer = 30059;

    //====================部落指挥命令==================================
    public static final int S2C_GuildCommand_Update = 30060;
    public static final int S2C_GuildCommand_Create = 30061;
    public static final int S2C_GuildCommand_Del = 30062;

    public static final int C2S_Guild_Impeach = 30063;//弹劾部落长
    public static final int S2C_Guild_Impeach = 30064;
    public static final int C2S_Guild_Deal = 30065;//处理弹劾部落长（是否同意）
    public static final int S2C_Guild_Deal  = 30066;
    public static final int C2S_Guild_AutoAdd = 30067;//修改部落自动添加成员标示
    public static final int S2C_Guild_AutoAdd  = 30068;
    public static final int C2S_Guild_TaskReward  = 30069;
    public static final int S2C_Guild_TaskReward  = 30070;
    public static final int S2C_Broad_GuildScoreChange  = 30071;//广播任务积分变化
    public static final int S2C_Broad_MemberIn = 30072;//部落成员登录或在线加入
    public static final int S2C_Broad_MemberLogout = 30073;//部落成员离线
    public static final int S2C_Broad_MemberQuit = 30074;//部落成员退出部落
    public static final int S2C_Broad_Guild_HaveCityReward = 30075;//广播城镇有奖励产出

    public static final int C2S_GuildDetailVo = 30076;//部落信息
    public static final int S2C_GuildDetailVo = 30077;
    // 贸易相关
    // 兑换
    public static final int C2S_Trade_Exchange = 30100;
    public static final int S2C_Trade_Exchange = 30101;
    // 启航
    public static final int C2S_Trade_Begin = 30102;
    public static final int S2C_Trade_Begin = 30103;
    // 改造
    public static final int C2S_Trade_Reform = 30104;
    public static final int S2C_Trade_Reform = 30105;
    // 回港
    public static final int C2S_Trade_Cancel = 30106;
    public static final int S2C_Trade_Cancel = 30107;
    // 买船
    public static final int C2S_Trade_Buy_Boat = 30108;
    public static final int S2C_Trade_Buy_Boat = 30109;
    // 航运公司升级
    public static final int C2S_Trade_Upgrade = 30110;
    public static final int S2C_Trade_Upgrade = 30111;
    // 同客户端同步状态
    public static final int C2S_Trade_Sync = 30112;
    public static final int S2C_Trade_Sync = 30113;
    // 手动交货
    public static final int C2S_Trade_Delivery = 30114;
    public static final int S2C_Trade_Delivery = 30115;
    // 切换自动启航状态
    public static final int C2S_Trade_Auto = 30116;
    public static final int S2C_Trade_Auto = 30117;
    // 获取收益记录
    public static final int C2S_Trade_Earnings = 30118;
    public static final int S2C_Trade_Earnings = 30119;
    // 新手引导
    public static final int C2S_Trade_Guide = 30120;
    public static final int S2C_Trade_Guide = 30121;
    // 航运公司升级秒时间
    public static final int C2S_Trade_UpgradeGold = 30122;
    public static final int S2C_Trade_UpgradeGold = 30123;
    // 航运船只升级秒时间
    public static final int C2S_Trade_ReformGold = 30124;
    public static final int S2C_Trade_ReformGold = 30125;

    // 贸易相关
    public static final int C2S_TradeStock_EditorTroop = 30130;
    public static final int S2C_TradeStock_EditorTroop = 30131;
    //打开贸易大股东界面
    public static final int C2S_TradeStock_Open = 30132;
    public static final int S2C_TradeStock_Open = 30133;
    public static final int C2S_TradeStock_GetDefTroop = 30134;
    public static final int S2C_TradeStock_GetDefTroop = 30135;
    public static final int C2S_TradeStock_WarOwner = 30136;//赶走大股东
    public static final int S2C_TradeStock_WarOwner = 30137;

    public static final int C2S_TradeStock_GiveUpOwner = 30138;//主动放弃大股东
    public static final int S2C_TradeStock_GiveUpOwner = 30139;

    public static final int C2S_TradeStock_WarOther = 30140;//和别人战斗
    public static final int S2C_TradeStock_WarOther = 30141;

    public static final int C2S_TradeStock_Rank = 30142;//获取战斗玩家列表
    public static final int S2C_TradeStock_Rank = 30143;

    public static final int C2S_TradeStock_GetOwner = 30144;//获取是否有大股东
    public static final int S2C_TradeStock_GetOwner = 30145;

    public static final int C2S_TradeStock_Record = 30146;//记录
    public static final int S2C_TradeStock_Record = 30147;

    //指挥官装备
    public static final int C2S_Equ_QuaLvUp = 31003;//装备品质合成
    public static final int S2C_Equ_QuaLvUp = 31004;
    public static final int C2S_Equ_Strengthen = 31005;//强化装备
    public static final int S2C_Equ_Strengthen = 31006;
    public static final int C2S_Equ_Strengthen_OneKey = 31007;//强化装备一键
    public static final int S2C_Equ_Strengthen_OneKey = 31008;


    public static final int C2S_Stone_LvUp_OneKey = 31015;//一键升级宝石
    public static final int S2C_Stone_LvUp_OneKey = 31016;

    public static final int C2S_Stone_Install_OneKey = 31017;//一键镶嵌宝石
    public static final int S2C_Stone_Install_OneKey = 31018;
    public static final int C2S_Stone_Change = 31011;//更换宝石
    public static final int S2C_Stone_Change = 31012;


    //三军演武
    public static final int S2C_ServerFunction_Change = 31101;//服务器解锁功能变换
    public static final int C2S_OverallWar_Troop_Change = 31103;//改变防守阵容
    public static final int S2C_OverallWar_Troop_Change = 31104;
    public static final int C2S_OverallWar_Match = 31105;//匹配
    public static final int S2C_OverallWar_Match = 31106;
    public static final int C2S_OverallWar_Fight = 31107;//战斗
    public static final int S2C_OverallWar_Fight = 31108;
    public static final int C2S_OverallWar_Open = 31109;//打开消息
    public static final int S2C_OverallWar_Open = 31110;
    public static final int C2S_OverallWar_Reward = 31111;//领取奖励
    public static final int S2C_OverallWar_Reward = 31112;
    public static final int C2S_OverallWar_Record = 31113;//战斗记录
    public static final int S2C_OverallWar_Record = 31114;


    //答题活动
    public static final int C2S_Question_Answer = 31201;//答题
    public static final int S2C_Question_Answer = 31202;

    public static final int C2S_Question_Open = 31203;//打开
    public static final int S2C_Question_Open = 31204;

    // 民情事件
    public static final int C2S_RandomTask_Accept = 31300;
    public static final int S2C_RandomTask_Accept = 31301;
    public static final int C2S_RandomTask_Reject = 31302;
    public static final int S2C_RandomTask_Reject = 31303;
    public static final int C2S_RandomTask_Refresh = 31304;
    public static final int S2C_RandomTask_Refresh = 31305;
    public static final int C2S_RandomTask_Reward = 31306;
    public static final int S2C_RandomTask_Reward = 31307;
    public static final int C2S_RandomTask_Finish = 31308;
    public static final int S2C_RandomTask_Finish = 31309;
    //邀请
    public static final int C2S_Invite_Bind = 31400;//绑定邀请（A使用B的邀请码进行绑定）
    public static final int S2C_Invite_Bind = 31401;
    public static final int C2S_Invite_Receive = 31402;//领取邀请奖励
    public static final int S2C_Invite_Receive = 31403;
    public static final int C2S_Invite_Receive_BeInvited = 31404;//领取被邀请奖励
    public static final int S2C_Invite_Receive_BeInvited = 31405;
    public static final int C2S_Invite_Open = 31406;//打开邀请
    public static final int S2C_Invite_Open = 31407;
    public static final int C2S_Invite_DayReceive = 31408;//领取每日邀请奖励
    public static final int S2C_Invite_DayReceive = 31409;

    public static final int C2S_Invite_ShareReward = 31412;//邀请分享
    public static final int S2C_Invite_ShareReward = 31413;

    //沙场点兵
    public static final int C2S_BattleCallSolider_TankPool = 31500;//发牌
    public static final int S2C_BattleCallSolider_TankPool = 31501;
    public static final int C2S_BattleCallSolider_TankChoice = 31502;//选择坦克
    public static final int S2C_BattleCallSolider_TankChoice = 31503;
    public static final int C2S_BattleCallSolider_GiveUp = 31504;//放弃坦克
    public static final int S2C_BattleCallSolider_GiveUp = 31505;
    public static final int C2S_BattleCallSolider_Fight_Start = 31506;//开始战斗
    public static final int S2C_BattleCallSolider_Fight_Start = 31507;
    public static final int C2S_BattleCallSolider_Fight = 31508;//战斗
    public static final int S2C_BattleCallSolider_Fight = 31509;
    public static final int C2S_BattleCallSolider_Init = 31510;//初始化
    public static final int S2C_BattleCallSolider_Init = 31511;
    public static final int C2S_BattleCallSolider_Reset = 31512;//重置
    public static final int S2C_BattleCallSolider_Reset = 31513;
    public static final int C2S_BattleCallSolider_ChoiceGiveUp = 31514;//选择弃牌
    public static final int S2C_BattleCallSolider_ChoiceGiveUp = 31515;
    public static final int C2S_BattleCallSolider_ResetAll = 31516;//全部重置
    public static final int S2C_BattleCallSolider_ResetAll = 31517;

    //七夕活动
    public static final int C2S_ValentineDay_Give = 31601;//赠送玫瑰
    public static final int S2C_ValentineDay_Give = 31602;
    public static final int C2S_ValentineDay_Fight = 31603;//战斗
    public static final int S2C_ValentineDay_Fight = 31604;
    public static final int C2S_ValentineDay_Cost = 31605;//消耗事件
    public static final int S2C_ValentineDay_Cost = 31606;
    public static final int C2S_ValentineDay_Lucy = 31607;//幸运事件
    public static final int S2C_ValentineDay_Lucy = 31608;
    public static final int C2S_ValentineDay_Buy = 31609;//买花
    public static final int S2C_ValentineDay_Buy = 31610;
    public static final int C2S_ValentineDay_Shopping = 31611;//购物
    public static final int S2C_ValentineDay_Shopping = 31612;

    //七夕活动
    public static final int C2S_ValentineDayNew_Give = 31613;//赠送玫瑰
    public static final int S2C_ValentineDayNew_Give = 31614;
    public static final int C2S_ValentineDayNew_Fight = 31615;//战斗
    public static final int S2C_ValentineDayNew_Fight = 31616;
    public static final int C2S_ValentineDayNew_Cost = 31617;//消耗事件
    public static final int S2C_ValentineDayNew_Cost = 31618;
    public static final int C2S_ValentineDayNew_Lucy = 31619;//幸运事件
    public static final int S2C_ValentineDayNew_Lucy = 31620;
    public static final int C2S_ValentineDayNew_Buy = 31621;//买花
    public static final int S2C_ValentineDayNew_Buy = 31622;
    public static final int C2S_ValentineDayNew_Shopping = 31623;//购物
    public static final int S2C_ValentineDayNew_Shopping = 31624;
    public static final int C2S_ValentineDayNew_Recharge = 31625;//节日累充，奖励领取
    public static final int S2C_ValentineDayNew_Recharge = 31626;
    public static final int C2S_ValentineDayNew_Gift = 31627;//节日礼包
    public static final int S2C_ValentineDayNew_Gift = 31628;

    //===================纪念馆===================================
    public static final int C2S_ShowPhoto = 31701;//贴照片
    public static final int S2C_ShowPhoto = 31702;
    public static final int C2S_ChapterLvUp = 31703;//章节升级
    public static final int S2C_ChapterLvUp = 31704;
    public static final int C2S_RecoveryPhoto = 31706;//章节升级
    public static final int S2C_RecoveryPhoto = 31707;


    //====================周年庆====================
    public static final int C2S_Anniversary_RechargeReward = 31801;//充值领奖
    public static final int S2C_Anniversary_RechargeReward = 31802;
    public static final int C2S_Anniversary_TaskBox = 31803;//领取任务盒子奖励
    public static final int S2C_Anniversary_TaskBox = 31804;
    public static final int C2S_Anniversary_CakeCreate = 31805;//蛋糕制作
    public static final int S2C_Anniversary_CakeCreate = 31806;
    public static final int C2S_Anniversary_CakeReward = 31807;//蛋糕奖励
    public static final int S2C_Anniversary_CakeReward = 31808;
    public static final int C2S_Anniversary_Fireworks = 31809;//燃放烟花
    public static final int S2C_Anniversary_Fireworks = 31810;//
    public static final int C2S_Anniversary_OpenBless = 31811;//打开祝福界面
    public static final int S2C_Anniversary_OpenBless = 31812;//
    public static final int C2S_Anniversary_ReceiveFrieworkdReward = 31813;//领取烟花奖励
    public static final int S2C_Anniversary_ReceiveFrieworkdReward = 31814;//
    public static final int C2S_Anniversary_ReceiveKfBox = 31815;//领取跨服箱子
    public static final int S2C_Anniversary_ReceiveKfBox = 31816;//




    //=================跨服======================
    public static final int C2S_KfSportsWorship = 32001;//膜拜
    public static final int S2C_KfSportsWorship = 32002;//

    public static final int S2C_KfMineInfo = 32003;//

    public static final int C2S_KfExpeditionDeclare = 32004;//跨服宣战
    public static final int S2C_KfExpeditionDeclare = 32005;//
    public static final int C2S_KfExpeditionServerList = 32006;//获取可以宣战的服务器列表
    public static final int S2C_KfExpeditionServerList = 32007;//

    public static final int C2S_ServerName = 32008;//获取服务器昵称
    public static final int S2C_ServerName = 32009;//
    //跨服段位赛王者信息
    public static final int S2C_KfPkKingInfo = 32010;//

    public static final int C2S_KfLevelRankRedPoint = 32011;//
    public static final int S2C_KfLevelRankRedPoint = 32012;//
    //跨服征讨红点
    public static final int S2C_KfBuildExtortRed = 32013;//

    public static final int C2S_KfBuildHunterLog = 32014;//
    public static final int S2C_KfBuildHunterLog = 32015;//

    public static final int C2S_KfUrl = 32016;//获取跨服对外映射
    public static final int S2C_KfUrl = 32017;//

    public static final int S2C_KfClose = 32018;//

    //=====================部落===========================
    public static final int C2S_GuildCityReward = 32021;//部落城池产出信息
    public static final int S2C_GuildCityReward = 32022;//部落城池产出信息
    public static final int C2S_GuildCityRewardDetail = 32023;//部落城池产出详细信息
    public static final int S2C_GuildCityRewardDetail = 32024;//部落城池产出详细信息


    //====================极地乱斗===============================
    public static final int C2S_KfScuffleSignup = 32030;//报名
    public static final int S2C_KfScuffleSignup = 32031;//

    public static final int C2S_GetSuffleSignInfo = 32032;//获取报名信息
    public static final int S2C_GetSuffleSignInfo = 32033;//

    //====================跨服世界大战===============================
    public static final int C2S_KfWorldWarUrl = 32050;//获取跨服世界大战连接地址
    public static final int S2C_KfWorldWarUrl = 32051;//
    public static final int C2S_KfWorldWarEndData = 32052;//获取跨服世界大战结束信息
    public static final int S2C_KfWorldWarEndData = 32053;//

    public static final int C2S_KfWorldWar_shop = 32054;//获取跨服世界大战 商店
    public static final int S2C_KfWorldWar_shop = 32055;//

    //====================跨服王者峡谷===============================
    public static final int C2S_KfKingSignup = 32056;//报名 无参数
    public static final int S2C_KfKingSignup = 32057;//

    public static final int C2S_getKingInfo = 32058;//获取当前10的信息
    public static final int S2C_KfKingPlayersUpdate = 32059;//返回信息

    public static final int C2S_KfKingChangeDef = 32060;//修改防守部队 string armys;
    public static final int S2C_KfKingChangeDef = 32061;//

    //====================赛季=================================
    public static final int C2S_KFSeasonRank = 32101;//获取赛季排行
    public static final int S2C_KFSeasonRank = 32102;//
    public static final int C2S_KFSeasonTop = 32103;//获取赛季风云榜
    public static final int S2C_KFSeasonTop = 32104;//
    public static final int C2S_KFSeason_Buy = 32105;//賽季商店购买
    public static final int S2C_KFSeason_Buy = 32106;//

    public static final int C2S_KFTask_Reward = 32107;//賽季任务
    public static final int S2C_KFTask_Reward = 32108;//
    public static final int C2S_KFTask_Detail = 32109;//賽季任务详情
    public static final int S2C_KFTask_Detail = 32110;//



    // 折扣狂欢
    public static final int C2S_MS_DiscountBuy = 33102;
    public static final int S2C_MS_DiscountBuy = 33103;
    public static final int C2S_MS_Sign = 33104;
    public static final int S2C_MS_Sign = 33105;
    //绑定手机号
    public static final int C2S_Bind_Phone = 33201;
    public static final int S2C_Bind_Phone = 33202;
    public static final int C2S_BindPhone_Receive = 33203;
    public static final int S2C_BindPhone_Receive = 33204;

    //资源找回
    public static final int C2S_ResBack = 33301;
    public static final int S2C_ResBack = 33302;
    public static final int C2S_ResBackAll = 33303;
    public static final int S2C_ResBackAll = 33304;


    //物资突袭
    public static final int C2S_ResSuddenStrike_End = 33401;
    public static final int S2C_ResSuddenStrike_End = 33402;
    public static final int C2S_ResSuddenStrike_BuyCount = 33403;
    public static final int S2C_ResSuddenStrike_BuyCount = 33404;

    //储备物资
    public static final int C2S_MaterialReserve_OpenBox = 33501;
    public static final int S2C_MaterialReserve_OpenBox = 33502;
    public static final int C2S_MaterialReserve_Buy = 33503;
    public static final int S2C_MaterialReserve_Buy = 33504;

    //专精
    public static final int C2S_Mastery_LvUp = 33601;
    public static final int S2C_Mastery_LvUp = 33602;

    //玩家部队战力排行详情
    public static final int C2S_TroopRank_Info = 33701;
    public static final int S2C_TroopRank_Info = 33702;

    //总统特权
    public static final int C2S_ServerPower_CenterCitys = 33901;//设置中心城市
    public static final int S2C_ServerPower_CenterCitys = 33902;
    public static final int C2S_ServerPower_MilitaryPolicy = 33903;//颁布军事政策
    public static final int S2C_ServerPower_MilitaryPolicy = 33904;
    public static final int C2S_ServerPower_Punish = 33905;//制裁
    public static final int S2C_ServerPower_Punish = 33906;
    public static final int C2S_ServerPower_PunishOpen = 33907;
    public static final int S2C_ServerPower_PunishOpen = 33908;

    //乘员系统
    public static final int C2S_Passenger_Compose = 34001;//乘员合成
    public static final int S2C_Passenger_Compose = 34002;
    public static final int C2S_Passenger_Up = 34003;//乘员上阵
    public static final int S2C_Passenger_Up = 34004;
    public static final int C2S_Passenger_Down = 34005;//乘员下阵
    public static final int S2C_Passenger_Down = 34006;
    public static final int C2S_Passenger_LevelUp = 34007;//升级
    public static final int S2C_Passenger_LevelUp = 34008;
    public static final int C2S_Passenger_Culture = 34009;//培养
    public static final int S2C_Passenger_Culture = 34010;
    public static final int C2S_Passenger_StarUp = 34011;//升星
    public static final int S2C_Passenger_StarUp = 34012;
    public static final int C2S_Passenger_Lock = 34013;//锁定
    public static final int S2C_Passenger_Lock = 34014;
    public static final int C2S_Passenger_UnLock = 34015;//解锁
    public static final int S2C_Passenger_UnLock = 34016;
    public static final int C2S_Passenger_Retire = 34017;//退役
    public static final int S2C_Passenger_Retire = 34018;
    public static final int C2S_Passenger_UpAll = 340019;//乘员一键上阵
    public static final int S2C_Passenger_UpAll = 340020;

    //现代战争
    public static final int C2S_ModernBattle_SetTroop = 340101;//上阵部队
    public static final int S2C_ModernBattle_SetTroop = 340102;
    public static final int C2S_ModernBattle_Choice = 340103;//选择事件
    public static final int S2C_ModernBattle_Choice = 340104;
    public static final int C2S_ModernBattle_ChangeTroop = 340105;//更换部队
    public static final int S2C_ModernBattle_ChangeTroop = 340106;
    public static final int C2S_ModernBattle_Fight = 340107;//战斗
    public static final int S2C_ModernBattle_Fight = 340108;
    public static final int C2S_ModernBattle_Sweep = 340109;//战斗
    public static final int S2C_ModernBattle_Sweep = 340110;

    //单兵奇袭
    public static final int C2S_RaidBattle_Start = 340201;//开始奇袭
    public static final int S2C_RaidBattle_Start = 340202;
    public static final int C2S_RaidBattle_Event = 340203;//单兵奇袭生成事件
    public static final int S2C_RaidBattle_Event = 340204;
    public static final int C2S_RaidBattle_Fight = 340205;//单兵奇袭战斗
    public static final int S2C_RaidBattle_Fight = 340206;
    public static final int C2S_RaidBattle_Active = 340207;//单兵奇袭激活boss
    public static final int S2C_RaidBattle_Active = 340208;
    public static final int C2S_RaidBattle_Receive = 340209;//单兵奇袭领取每周进度奖励
    public static final int S2C_RaidBattle_Receive = 340210;
    public static final int S2C_RaidBattle_End = 340212;//奇袭结束
    public static final int C2S_RaidBattle_Sweep = 340213;//单兵奇袭扫荡
    public static final int S2C_RaidBattle_Sweep = 340214;


    //坦克魔改
    public static final int C2S_Tank_MagicReform = 340301;//魔改
    public static final int S2C_Tank_MagicReform = 340302;
    public static final int C2S_Tank_MagicReform_Transfer = 340303;//魔改转移
    public static final int S2C_Tank_MagicReform_Transfer = 340304;
    public static final int C2S_Tank_MagicReform_Reset = 340305;//魔改重置
    public static final int S2C_Tank_MagicReform_Reset  = 340306;

    //奇兵
    public static final int C2S_Tank_Soldier_Bind = 340311;//绑定
    public static final int S2C_Tank_Soldier_Bind = 340312;
    public static final int C2S_Tank_Soldier_UnBind = 340313;//解绑
    public static final int S2C_Tank_Soldier_UnBind = 340314;
    public static final int C2S_Tank_Soldier_LvUp = 340315;//升级
    public static final int S2C_Tank_Soldier_LvUp = 340316;
    public static final int C2S_Tank_Soldier_OpenChoice = 340317;//打开选择框
    public static final int S2C_Tank_Soldier_OpenChoice = 340318;

    //坦克强化
    public static final int C2S_TankStrength = 340321;//强化
    public static final int S2C_TankStrength = 340322;
    public static final int C2S_TankStrength_Breach = 340323;//强化突破
    public static final int S2C_TankStrength_Breach = 340324;

    public static final int C2S_TankFight_Start = 340325;//坦克大战开始
    public static final int S2C_TankFight_Start = 340326;
    public static final int C2S_TankFight = 340327;//坦克大战
    public static final int S2C_TankFight = 340328;


    public static final int C2S_TankDriverAd = 340329;//坦克车长军职升级
    public static final int S2C_TankDriverAd = 340330;

    public static final int C2S_TankDriverAdQuit = 340331;//坦克车长军职退伍
    public static final int S2C_TankDriverAdQuit = 340332;

    public static final int C2S_TankTrain_Sweep = 340340;//坦克训练扫荡
    public static final int S2C_TankTrain_Sweep = 340341;

    public static final int C2S_TankJingzhu_Random = 340343;//坦克精铸，随机属性
    public static final int S2C_TankJingzhu_Random = 340344;
    public static final int C2S_TankJingzhu_Lvup = 340345;//坦克精铸，等级升级
    public static final int S2C_TankJingzhu_Lvup = 340346;
    public static final int C2S_TankJingzhu_LockUnlock = 340347;//坦克精铸，锁定属性
    public static final int S2C_TankJingzhu_LockUnlock = 340348;


    //巅峰挑战
    public static final int C2S_PeakBattle_choice = 340401;//选择对手
    public static final int S2C_PeakBattle_choice = 340402;
    public static final int C2S_PeakBattle_next = 340403;//挑战下一关
    public static final int S2C_PeakBattle_next = 340404;
    public static final int C2S_PeakBattle_sweep = 340405;//扫荡
    public static final int S2C_PeakBattle_sweep = 340406;
    public static final int C2S_PeakBattle_StarReward = 340407;//领取星级奖励
    public static final int S2C_PeakBattle_StarReward = 340408;
    public static final int C2S_PeakBattle_FightStart = 340409;//开始战斗
    public static final int S2C_PeakBattle_FightStart = 340410;
    public static final int C2S_PeakBattle_Fight = 340411;//战斗
    public static final int S2C_PeakBattle_Fight = 340412;

    //梦回沙场
    public static final int C2S_DreamBattle_Start = 340421;//开始挑战
    public static final int S2C_DreamBattle_Start = 340222;
    public static final int C2S_DreamBattle_Fight = 340423;//战斗
    public static final int S2C_DreamBattle_Fight = 340224;
    public static final int C2S_DreamBattle_FightElite = 340425;//精英战斗
    public static final int S2C_DreamBattle_FightElite = 340226;
    public static final int C2S_DreamBattle_Reset = 340427;//重置
    public static final int S2C_DreamBattle_Reset = 340228;
    public static final int C2S_DreamBattle_ReceiveBox = 340429;//领取箱子奖励
    public static final int S2C_DreamBattle_ReceiveBox = 340230;
    public static final int C2S_DreamBattle_FightStart = 340431;//战斗开始检查
    public static final int S2C_DreamBattle_FightStart = 340232;
    public static final int C2S_DreamBattle_Sweep = 340433;//战斗开始检查
    public static final int S2C_DreamBattle_Sweep = 340434;

    //新手引导跳过
    public static final int C2S_SkipInfo = 340501;//请求玩家是否能够跳过新手引导
    public static final int S2C_SkipInfo = 340502;
    public static final int C2S_SkipGuide = 340503;//跳过新手引导
    public static final int S2C_SkipGuide = 340504;
    //神秘商店
    public static final int C2S_MysteryShopBuy = 340511;//神秘商店购买
    public static final int S2C_MysteryShopBuy = 340512;

    //圣诞活动
    public static final int C2S_Christmas_Wish = 340601;//许愿
    public static final int S2C_Christmas_Wish = 340602;
    public static final int C2S_Christmas_DressUp = 340603;//装扮
    public static final int S2C_Christmas_DressUp = 340604;
    public static final int C2S_Christmas_Receive = 340605;//领取进度奖励
    public static final int S2C_Christmas_Receive = 340606;
    public static final int C2S_Christmas_Gift = 340607;//礼物
    public static final int S2C_Christmas_Gift = 340608;
    public static final int C2S_Christmas_WishRecord = 340609;//许愿记录
    public static final int S2C_Christmas_WishRecord = 340610;

    //活动任务领取
    public static final int C2S_Activity_TaskReceive = 340701;//活动任务领取
    public static final int S2C_Activity_TaskReceive = 340702;
    //活动商店购买
    public static final int C2S_Activity_ShopBuy = 340703;//活动商店购买
    public static final int S2C_Activity_ShopBuy = 340704;
    public static final int C2S_Activity_Addr = 340705;//活动添加收货地址
    public static final int S2C_Activity_Addr = 340710;
    public static final int C2S_Activity_73_Choice_Stage = 340706;//连续充值选择档位
    public static final int S2C_Activity_73_Choice_Stage = 340707;
    public static final int C2S_Activity_125_Choice_Stage = 340708;//连续充值选择档位
    public static final int S2C_Activity_125_Choice_Stage = 340709;


    public static final int C2S_Activity_KF_GetRank = 340803;
    public static final int S2C_Activity_KF_GetRank = 340804;

    //跨服分组排行
    public static final int C2S_Activity_KF_Group_GetRank = 340805;
    public static final int S2C_Activity_KF_Group_GetRank = 340806;

    //充值狂欢
    public static final int C2S_Activity_ConsumeCarnival_RankReward = 340807;
    public static final int S2C_Activity_ConsumeCarnival_RankReward = 340808;

    //领取签到轮数奖励
    public static final int C2S_Sign_RoundReward = 340811;
    public static final int S2C_Sign_RoundReward = 340812;

    //建筑

    //队列
    public static final int C2S_Queue_TimeOver = 340821;//队列时间到
    public static final int C2S_Queue_SpeedUp = 340822;//加速队列
    public static final int C2S_Queue_CollectionProduct = 340823;//收取队列
    public static final int S2C_Queue_CollectionProduct = 340824;
    public static final int C2S_Queue_Cancel = 340825;//队列取消
    public static final int S2C_Queue_Cancel = 340826;//队列取消
    public static final int C2S_UnlockBuild = 340829;//解锁建筑
    public static final int S2C_UnlockBuild = 340830;
    public static final int C2S_BuildLvUp = 340831;//建筑升级
    public static final int S2C_BuildLvUp = 340832;
    public static final int C2S_BuildCreate = 340833;//建筑建造
    public static final int S2C_BuildCreate = 340834;
    public static final int C2S_Queue_Buy = 340835;//购买队列
    public static final int S2C_Queue_Buy = 340836;
    public static final int C2S_Build_Res_Collect = 340837;//收矿
    public static final int S2C_Build_Res_Collect = 340838;
    public static final int C2S_Build_ChangeBlock = 340839;//交换位置
    public static final int S2C_Build_ChangeBlock = 340840;//交换位置
    public static final int C2S_Build_Product = 340841;//生产
    public static final int S2C_Build_Product = 340842;
    public static final int C2S_Build_Auto = 340843;//自动建造开启关闭
    public static final int S2C_Build_Auto = 340844;

    //秘境宝藏
    public static final int C2S_RareTreasure_Choice = 340901;//秘境宝藏选择难度
    public static final int S2C_RareTreasure_Choice = 340902;//秘境宝藏选择难度
    public static final int C2S_RareTreasure_Open = 340903;//打开点
    public static final int S2C_RareTreasure_Open = 340904;//
    public static final int C2S_RareTreasure_Use = 340905;//使用地图上的道具
    public static final int S2C_RareTreasure_Use = 340906;//
    public static final int C2S_RareTreasure_Fight = 340907;//战斗
    public static final int S2C_RareTreasure_Fight = 340908;//
    public static final int C2S_RareTreasure_FightStart = 340909;//战斗开始
    public static final int S2C_RareTreasure_FightStart = 340910;//
    public static final int C2S_RareTreasure_Sweep = 340911;//秘境宝藏扫荡
    public static final int S2C_RareTreasure_Sweep = 340912;//
    public static final int C2S_RareTreasure_End = 340913;//结束探宝
    public static final int S2C_RareTreasure_End = 340914;//


    //部落派兵
    public static final int C2S_GuildBarrack_Dispatch = 330301;//派兵到大营
    public static final int S2C_GuildBarrack_Dispatch = 330302;
    public static final int C2S_GuildBarrack_Retreat = 330303;//撤回
    public static final int S2C_GuildBarrack_Retreat = 330304;
    public static final int C2S_GuildBarrack_Repatriate = 330305;//遣返
    public static final int S2C_GuildBarrack_Repatriate = 330306;
    public static final int C2S_GuildBarrack_Expedition = 330307;//出征
    public static final int S2C_GuildBarrack_Expedition = 330308;
    public static final int C2S_GuildBarrack_Repair = 330309;//修理
    public static final int S2C_GuildBarrack_Repair = 330310;
    public static final int C2S_GuildBarrack_Open = 330311;//打开
    public static final int S2C_GuildBarrack_Open = 330312;

    //军工厂
    public static final int C2S_GuildFactory_Open = 330321;//打开
    public static final int S2C_GuildFactory_Open = 330322;
    public static final int C2S_GuildFactory_Build = 330323;//建设
    public static final int S2C_GuildFactory_Build = 330324;
    public static final int C2S_GuildFactory_Produce = 330325;//生产(提交证书)
    public static final int S2C_GuildFactory_Produce = 330326;
    public static final int C2S_GuildFactory_OpenAllot = 330327;//打开分配界面
    public static final int S2C_GuildFactory_OpenAllot = 330328;
    public static final int C2S_GuildFactory_Allot = 330329;//分配
    public static final int S2C_GuildFactory_Allot = 330330;
    public static final int C2S_GuildFactory_Strength = 330331;//强化
    public static final int S2C_GuildFactory_Strength = 330332;
    public static final int C2S_GuildFactory_BuildArms = 330333;//建造武器
    public static final int S2C_GuildFactory_BuildArms = 330334;
    public static final int S2C_GuildFactory_ArmsPosChange = 330335;//通知玩家武器位发生变化
    public static final int C2S_GuildFactory_StrengthFinish = 330337;//强化
    public static final int S2C_GuildFactory_StrengthFinish = 330338;
    public static final int C2S_GuildFactory_ArmsUp = 330339;//上阵或替换
    public static final int S2C_GuildFactory_ArmsUp = 330340;
    public static final int C2S_GuildFactory_ArmsDown = 330341;//下阵
    public static final int S2C_GuildFactory_ArmsDown = 330342;
    public static final int C2S_GuildFactory_BuyPos = 330343;//购买位置
    public static final int S2C_GuildFactory_BuyPos = 330344;
    public static final int C2S_Repair_Train = 330345;//维修训练
    public static final int S2C_Repair_Train = 330346;
    public static final int C2S_Arms_Decompose = 330347;//分解
    public static final int S2C_Arms_Decompose = 330348;
    public static final int C2S_GuildFactory_OpenRecord = 330349;//打开记录
    public static final int S2C_GuildFactory_OpenRecord = 330350;

    public static final int C2S_GuildFactory_Destory = 330351;//丢弃
    public static final int S2C_GuildFactory_Destory = 330352;
    // 登录基金活动玩家购买
    public static final int C2S_Login_Fund_Buy = 330400;
    public static final int S2C_Login_Fund_Buy = 330401;

    // 藏宝图活动
    public static final int C2S_Treasure_Map_Composite = 330500; //合成
    public static final int S2C_Treasure_Map_Composite = 330501;
    public static final int C2S_Treasure_Map_Open = 330502; // 开图
    public static final int S2C_Treasure_Map_Open = 330503;
    public static final int C2S_Treasure_Map_Hunter = 330504; // 宝藏猎人加奖励
    public static final int S2C_Treasure_Map_Hunter = 330505;

    // 特工
    public static final int C2S_Agent_Active = 330600;//激活
    public static final int S2C_Agent_Active = 330601;
    public static final int C2S_Agent_BuyTimes = 330602;//买体力
    public static final int S2C_Agent_BuyTimes = 330603;
    public static final int C2S_Agent_Training = 330604;//调教
    public static final int S2C_Agent_Training = 330605;
    public static final int C2S_Agent_Reward = 330606;//打赏
    public static final int S2C_Agent_Reward = 330607;
    public static final int C2S_Agent_Action_feedback = 330608;//回馈
    public static final int S2C_Agent_Action_feedback = 330609;
    public static final int C2S_Agent_Upgrade_Skill = 330610;//升级天赋
    public static final int S2C_Agent_Upgrade_Skill = 330611;
    public static final int C2S_Agent_Use_Item = 330612;//体力丹
    public static final int S2C_Agent_Use_Item = 330613;

    // 特工来访
    public static final int C2S_Agent_Come_Single = 330700;//单抽
    public static final int S2C_Agent_Come_Single = 330701;
    public static final int C2S_Agent_Come_Ten = 330702;//十连抽
    public static final int S2C_Agent_Come_Ten = 330703;
    public static final int C2S_Agent_Come_Log_Update = 330704;//日志有更新
    public static final int S2C_Agent_Come_Log_Update = 330705;
    public static final int C2S_Agent_Come_Logs = 330706;//获取日志
    public static final int S2C_Agent_Come_Logs = 330707;

    // 转盘
    public static final int C2S_Activity_101_Circle = 330800;
    public static final int S2C_Activity_101_Circle = 330801;
    // 转盘 补签
    public static final int C2S_Activity_101_Circle_Rescue = 330802;
    public static final int S2C_Activity_101_Circle_Rescue = 330803;
    // 祈福
    public static final int C2S_Activity_101_Pray = 330804;
    public static final int S2C_Activity_101_Pray = 330805;
    // 额外获得一次奖励次数
    public static final int C2S_Activity_101_Pray_Extra = 330806;
    public static final int S2C_Activity_101_Pray_Extra = 330807;
    // 商店购买
    public static final int C2S_Activity_101_Shop_Buy = 330808;
    public static final int S2C_Activity_101_Shop_Buy = 330809;
    // 商店重置
    public static final int C2S_Activity_101_Shop_Reset = 330810;
    public static final int S2C_Activity_101_Shop_Reset = 330811;
    // 投资
    public static final int C2S_Activity_101_Treasure = 330812;
    public static final int S2C_Activity_101_Treasure = 330813;
    // 祈福记录查询
    public static final int C2S_Activity_101_Pray_Logs = 330814;
    public static final int S2C_Activity_101_Pray_Logs = 330815;
    public static final int C2S_Activity_101_Treasure_Logs_Notify = 330816;
    public static final int S2C_Activity_101_Treasure_Logs_Notify = 330817;
    public static final int C2S_Activity_101_Treasure_Logs = 330818;
    public static final int S2C_Activity_101_Treasure_Logs = 330819;

    // 限时商店活动购买道具
    public static final int C2S_Activity_Limit_Discount_Buy = 330900;
    public static final int S2C_Activity_Limit_Discount_Buy = 330901;

    // 派遣
    public static final int C2S_MilitaryLineup_Update = 331000;
    public static final int S2C_MilitaryLineup_Update = 331001;
    // 升级助阵
    public static final int C2S_MilitaryLineup_Update_Assistance = 331002;
    public static final int S2C_MilitaryLineup_Update_Assistance = 331003;
    // 升级等级
    public static final int C2S_MilitaryLineup_LvUp = 331004;
    public static final int S2C_MilitaryLineup_LvUp = 331005;
    // 派遣全部
    public static final int C2S_MilitaryLineup_UpdateAll = 331006;
    public static final int S2C_MilitaryLineup_UpdateAll = 331007;

    // 在线转盘活动购买碎片
    public static final int C2S_Activity6_Buy = 331008;
    public static final int S2C_Activity6_Buy = 331009;

    // 每日必买活动兑换局座碎片
    public static final int C2S_Activity45_Exchange = 331010;
    public static final int S2C_Activity45_Exchange = 331011;

    // 双11活动刮奖
    public static final int C2S_Activity61_Scrap = 331012;
    public static final int S2C_Activity61_Scrap = 331013;

    // 双11活动获取奖励记录
    public static final int C2S_Activity61_Logs = 331014;
    public static final int S2C_Activity61_Logs = 331015;

    // 双11活动获取奖励记录
    public static final int C2S_Activity61_Before_Buy = 331016;
    public static final int S2C_Activity61_Before_Buy = 331017;

    public static final int C2S_Activity61_Refresh = 331018;
    public static final int S2C_Activity61_Refresh = 331019;

    public static final int C2S_Activity61_Ticket_Rec = 331020;
    public static final int S2C_Activity61_Ticket_Rec = 331021;

    //老兵回归--奖励领取
    public static final int C2S_ActivitySoldier_Reward = 331022;
    public static final int S2C_ActivitySoldier_Reward = 331023;
    //老兵回归--跨服--是否是跨服，校验
    public static final int C2S_ActivitySoldier_KfCheck = 331024;
    public static final int S2C_ActivitySoldier_KfCheck = 331025;
    //老兵回归--跨服--是否是跨服，测试
    public static final int C2S_ActivitySoldier_KfTest = 331026;
    public static final int S2C_ActivitySoldier_KfTest = 331027;
    //抽折扣活动，限时折扣（新）--奖励领取
    public static final int C2S_Discount_Reward = 331028;
    public static final int S2C_Discount_Reward = 331029;
    //抽折扣
    public static final int C2S_Discount_Random = 331030;
    public static final int S2C_Discount_Random = 331031;
    //老兵回归--任务奖励
    public static final int C2S_ActivitySoldier_TaskReward = 331032;
    public static final int S2C_ActivitySoldier_TaskReward = 331033;

    //元旦乐翻天--每日任务奖励领取
    public static final int C2S_Activity69_TaskReward = 331034;
    public static final int S2C_Activity69_TaskReward = 331035;
    //随机奖励
    public static final int C2S_Activity69_RandomReward = 331036;
    public static final int S2C_Activity69_RandomReward = 331037;
    //保存格子
    public static final int C2S_Activity69_SaveCell = 331038;
    public static final int S2C_Activity69_SaveCell = 331039;
    //禁用格子
    public static final int C2S_Activity69_DisableCell = 331040;
    public static final int S2C_Activity69_DisableCell = 331041;
    //一键解锁
    public static final int C2S_Activity69_Unlock = 331042;
    public static final int S2C_Activity69_Unlock = 331043;
    //购买游戏币
    public static final int C2S_Activity69_BuyCoins = 331044;
    public static final int S2C_Activity69_BuyCoins = 331045;
    //奖励领取记录
    public static final int C2S_Activity69_ItemsLog = 331046;
    public static final int S2C_Activity69_ItemsLog = 331047;

    //抽折扣
    public static final int C2S_Discount_Reset = 331048;
    public static final int S2C_Discount_Reset = 331049;

    // 双12活动刮奖
    public static final int C2S_Activity70_Scrap = 331112;
    public static final int S2C_Activity70_Scrap = 331113;

    // 双12活动获取奖励记录
    public static final int C2S_Activity70_Logs = 331114;
    public static final int S2C_Activity70_Logs = 331115;

    // 双12活动获取奖励记录
    public static final int C2S_Activity70_Before_Buy = 331116;
    public static final int S2C_Activity70_Before_Buy = 331117;

    public static final int C2S_Activity70_Refresh = 331118;
    public static final int S2C_Activity70_Refresh = 331119;

    public static final int C2S_Activity70_Ticket_Rec = 331120;
    public static final int S2C_Activity70_Ticket_Rec = 331121;

    // vip认证
    public static final int C2S_Vip_Prove = 331122;
    public static final int S2C_Vip_Prove = 331123;

    public static final int C2S_Activity_MergeLantern = 331201;//元宵节活动--合成月饼
    public static final int S2C_Activity_MergeLantern = 331202;
    public static final int C2S_Activity_LanternResource = 331203;//元宵节活动--领取材料礼盒（统计金砖消耗 获取）
    public static final int S2C_Activity_LanternResource = 331204;
    public static final int C2S_Activity_LanternBuyGift = 331205;//元宵节活动--中秋好礼（金砖购买）
    public static final int S2C_Activity_LanternBuyGift = 331206;
    public static final int C2S_Activity_LanternBuyTimeGift = 331207;//元宵节活动-- 那兔送福（金砖购买）
    public static final int S2C_Activity_LanternBuyTimeGift = 331208;

    public static final int C2S_Activity_Spring7Cycle = 331301;//过年7天乐--转盘活动
    public static final int S2C_Activity_Spring7Cycle = 331302;
    public static final int C2S_Activity_Spring7OpenShop = 331303;//过年7天乐--开启神秘商店
    public static final int S2C_Activity_Spring7OpenShop = 331304;
    public static final int C2S_Activity_Spring7AddNum = 331305;//过年7天乐--添加小游戏次数
    public static final int S2C_Activity_Spring7AddNum = 331306;
    public static final int C2S_Activity_Spring7Buy = 331307;//过年7天乐--购买礼包
    public static final int S2C_Activity_Spring7Buy = 331308;
    public static final int C2S_Activity_GetGameReward = 331309;//过年7天乐--领取礼包
    public static final int S2C_Activity_GetGameReward = 331310;
    public static final int C2S_Activity_GetEndReward = 331311;//过年7天乐--最终奖励领取
    public static final int S2C_Activity_GetEndReward = 331312;
    public static final int C2S_Activity_Skip = 331313;//过年7天乐--跳过
    public static final int S2C_Activity_Skip = 331314;
    public static final int C2S_Activity_GameReset = 331315;//过年7天乐--小游戏重置
    public static final int S2C_Activity_GameReset = 331316;

    //充值转盘
    public static final int C2S_Recharge_circle_Rec = 331401; // 领取奖励
    public static final int S2C_Recharge_circle_Rec = 331402;
    public static final int C2S_Recharge_circle_Record = 331403; // 领奖记录
    public static final int S2C_Recharge_circle_Record = 331404;


    // 合字
    public static final int C2S_Activity74_Compose = 331500;
    public static final int S2C_Activity74_Compose = 331501;
    // 开红包
    public static final int C2S_Activity74_RedBag = 331502;
    public static final int S2C_Activity74_RedBag = 331503;
    // 财神降临
    public static final int C2S_Activity74_Treasure = 331504;
    public static final int S2C_Activity74_Treasure = 331505;
    public static final int C2S_Activity74_TreasureLog = 331506;
    public static final int S2C_Activity74_TreasureLog = 331507;
    public static final int C2S_Activity74_TreasureLogNotify = 331508;
    public static final int S2C_Activity74_TreasureLogNotify = 331509;
    // 充值返利
    public static final int C2S_Activity74_Rebate = 331510;
    public static final int S2C_Activity74_Rebate = 331511;
    // 驱赶年兽
    public static final int C2S_Activity74_HitBoss = 331512;
    public static final int S2C_Activity74_HitBoss = 331513;
    // 购买驱赶年兽次数
    public static final int C2S_Activity74_HitBoss_BuyTimes = 331514;
    public static final int S2C_Activity74_HitBoss_BuyTimes = 331515;
    // 领取打死年兽的奖励
    public static final int C2S_Activity74_HitBoss_Reward = 331516;
    public static final int S2C_Activity74_HitBoss_Reward = 331517;
    // 获取红包部分公共数据
    public static final int C2S_Activity74_RedBag_Data = 331518;
    public static final int S2C_Activity74_RedBag_Data = 331519;

    // 1.11活动刮奖
    public static final int C2S_Activity79_Scrap = 331612;
    public static final int S2C_Activity79_Scrap = 331613;

    // 1.11活动获取奖励记录
    public static final int C2S_Activity79_Logs = 331614;
    public static final int S2C_Activity79_Logs = 331615;

    // 1.11活动获取奖励记录
    public static final int C2S_Activity79_Before_Buy = 331616;
    public static final int S2C_Activity79_Before_Buy = 331617;

    public static final int C2S_Activity79_Refresh = 331618;
    public static final int S2C_Activity79_Refresh = 331619;

    public static final int C2S_Activity79_Ticket_Rec = 331620;
    public static final int S2C_Activity79_Ticket_Rec = 331621;

    //七日充值活动，最终奖励领取
    public static final int C2S_Activity18_EndReward = 331701;
    public static final int S2C_Activity18_EndReward = 331702;


    // 清明活动领取充值奖励
    public static final int C2S_Activity_RichMan_RechargePrize = 331703;
    public static final int S2C_Activity_RichMan_RechargePrize = 331704;

    //331706---331718,占用
    //二月二--每日任务奖励领取
    public static final int C2S_Activity202_TaskReward = 331705;
    public static final int S2C_Activity202_TaskReward = 331706;
    //随机奖励
    public static final int C2S_Activity202_RandomReward = 331707;
    public static final int S2C_Activity202_RandomReward = 331708;
    //保存格子
    public static final int C2S_Activity202_SaveCell = 331709;
    public static final int S2C_Activity202_SaveCell = 331710;
    //禁用格子
    public static final int C2S_Activity202_DisableCell = 331711;
    public static final int S2C_Activity202_DisableCell = 331712;
    //一键解锁
    public static final int C2S_Activity202_Unlock = 331713;
    public static final int S2C_Activity202_Unlock = 331714;
    //购买游戏币
    public static final int C2S_Activity202_BuyCoins = 331715;
    public static final int S2C_Activity202_BuyCoins = 331716;
    //奖励领取记录
    public static final int C2S_Activity202_ItemsLog = 331717;
    public static final int S2C_Activity202_ItemsLog = 331718;

    // 清明活动领取成就奖励
    public static final int C2S_Activity_RichMan_ThrowPrize = 331719;
    public static final int S2C_Activity_RichMan_ThrowPrize = 331720;

    // 军备竞赛-随机订单
    public static final int C2S_Activity_ArmyRace_RandomOrder = 331721;
    public static final int S2C_Activity_ArmyRace_RandomOrder = 331722;
    // 军备竞赛-领取奖励
    public static final int C2S_Activity_ArmyRace_Reward = 331723;
    public static final int S2C_Activity_ArmyRace_Reward = 331724;
    // 军备竞赛-军力奖励
    public static final int C2S_Activity_ArmyPower_Reward = 331725;
    public static final int S2C_Activity_ArmyPower_Reward = 331726;


    // 一元夺宝打开界面
    public static final int C2S_Activity_OneDollar_Open = 331727;
    public static final int S2C_Activity_OneDollar_Open = 331728;
    // 一元夺宝打开投注
    public static final int C2S_Activity_OneDollar_Buy = 331729;
    public static final int S2C_Activity_OneDollar_Buy = 331730;
    // 消费宝塔-抽奖
    public static final int C2S_Activity_ConsumeTower = 331731;
    public static final int S2C_Activity_ConsumeTower = 331732;
    // 消费宝塔-重置
    public static final int C2S_Activity_ConsumeTower_Reset = 331733;
    public static final int S2C_Activity_ConsumeTower_Reset = 331734;
    // 一元夺宝买断
    public static final int C2S_Activity_OneDollar_BuyAll = 331735;
    public static final int S2C_Activity_OneDollar_BuyAll = 331736;

    // 海军节-弹药消耗
    public static final int C2S_Activity_Navy_Cost = 331801;
    public static final int S2C_Activity_Navy_Cost = 331802;
    // 海军节-击中目标
    public static final int C2S_Activity_Navy_Hit = 331803;
    public static final int S2C_Activity_Navy_Hit = 331804;
    // 海军节-被击中
    public static final int C2S_Activity_Navy_Hurt = 331805;
    public static final int S2C_Activity_Navy_Hurt = 331806;
    // 海军节-恢复耐久
    public static final int C2S_Activity_Navy_Regain = 331807;
    public static final int S2C_Activity_Navy_Regain = 331808;

    // 五一-探索矿洞
    public static final int C2S_Activity_51_Explore = 331809;
    public static final int S2C_Activity_51_Explore = 331810;
    // 五一-领取奖励
    public static final int C2S_Activity_51_Reward = 331811;
    public static final int S2C_Activity_51_Reward = 331812;
    // 五一-投资日志
    public static final int C2S_Activity_51_treasure_Logs = 331813;
    public static final int S2C_Activity_51_treasure_Logs = 331814;
    // 五一-投资
    public static final int C2S_Activity_51_Treasure = 331815;
    public static final int S2C_Activity_51_Treasure = 331816;
    // 广播
    public static final int S2C_Activity_51_Treasure_Logs_Notify = 331817;
    // 五一-新手引导
    public static final int C2S_Activity_51_Guide = 331819;
    public static final int S2C_Activity_51_Guide = 331820;

    // 海军节-购买弹药
    public static final int C2S_Activity_Navy_Buy = 331821;
    public static final int S2C_Activity_Navy_Buy = 331822;

    public static final int C2S_Activity55_MergeCake = 331831;//端午节活动--合成粽子
    public static final int S2C_Activity55_MergeCake = 331832;
    public static final int C2S_Activity55_Resource = 331833;//端午节活动--领取材料礼盒（统计金砖消耗 获取）
    public static final int S2C_Activity55_Resource = 331834;
    public static final int C2S_Activity55_BuyGift = 331835;//端午节活动--金砖购买
    public static final int S2C_Activity55_BuyGift = 331836;
    public static final int C2S_Activity55_ReceiveGift = 331837;//端午节活动
    public static final int S2C_Activity55_ReceiveGift = 331838;


    public static final int C2S_ActivityCustomize_Choise = 331851;//私人定制，礼包的选择保存
    public static final int S2C_ActivityCustomize_Choise = 331852;

    public static final int C2S_ActivityCustomize_Reset = 331853;//私人定制，重置礼包选择
    public static final int S2C_ActivityCustomize_Reset = 331854;

    public static final int C2S_CityWarSkill_Reward = 331901;//记录玩家偷袭、突进和撤退次数，奖励领取
    public static final int S2C_CityWarSkill_Reward = 331902;
    public static final int C2S_CityWarSkill_Lvup = 331903;//记录玩家偷袭、突进和撤退，技能升级
    public static final int S2C_CityWarSkill_Lvup = 331904;

    //=========================
    //新版81活动
    public static final int C2S_Act81_RechargeReward = 331905;//充值领奖
    public static final int S2C_Act81_RechargeReward = 331906;

    public static final int C2S_Act81_Pray_Logs = 331907; // 祈福记录查询  (节日庆典)
    public static final int S2C_Act81_Pray_Logs = 331908;
    public static final int C2S_Act81_Treasure_Logs = 331909;
    public static final int S2C_Act81_Treasure_Logs = 331910;
    public static final int C2S_Act81_Pray = 331911; // 祈福 (节日庆典)
    public static final int S2C_Act81_Pray = 331912;
    public static final int C2S_Act81_Shop_Buy = 331913;// 商店购买
    public static final int S2C_Act81_Shop_Buy = 331914;
    public static final int C2S_Act81_Shop_Reset = 331915; // 商店重置
    public static final int S2C_Act81_Shop_Reset = 331916;
    public static final int C2S_Act81_Treasure = 331917;//投资，财神降临
    public static final int S2C_Act81_Treasure = 331918;
    public static final int C2S_Act81_Treasure_Logs_Notify = 331919; //广播日志信息
    public static final int S2C_Act81_Treasure_Logs_Notify = 331920;
    public static final int C2S_Act81_Pray_Extra = 331921;// 额外获得一次奖励次数
    public static final int S2C_Act81_Pray_Extra = 331922;
    public static final int C2S_Act81_Bullet_Screen = 331923;// 发送聊天弹幕
    public static final int S2C_Act81_Bullet_Screen = 331924;
    public static final int C2S_Act81_Bullet_Screen_logs = 331925;// 获取弹幕内容
    public static final int S2C_Act81_Bullet_Screen_logs = 331926;

    //战俘营
    public static final int C2S_Captive_Buy_Researcher = 332001; //购买研究员
    public static final int S2C_Captive_Buy_Researcher = 332002;
    public static final int C2S_Captive_Change_Researcher = 332003; //更换研究员
    public static final int S2C_Captive_Change_Researcher = 332004;
    public static final int C2S_Captive_Lv_Up = 332005; //升级
    public static final int S2C_Captive_Lv_Up = 332006;
    public static final int C2S_Captive_Research = 332007; //研究
    public static final int S2C_Captive_Research = 332008;
    public static final int C2S_Captive_Auto_Tech = 332009; //自动研究设置
    public static final int S2C_Captive_Auto_Tech = 332010;
    public static final int C2S_Captive_Logs = 332011; //战俘记录
    public static final int S2C_Captive_Logs = 332012;
    public static final int C2S_Captive_Redeem = 332013; //赎回
    public static final int S2C_Captive_Redeem = 332014;


    public static final int C2S_Captive_EndTime_Check = 332015; //到期检查
    public static final int S2C_Captive_EndTime_Check = 332016;

    //六周年活动（狂欢节）
    public static final int C2S_Act97_RechargeReward = 332101;//签到奖励领奖
    public static final int S2C_Act97_RechargeReward = 332102;

    public static final int C2S_Act97_Cycle = 332103;//充值转盘
    public static final int S2C_Act97_Cycle = 332104;

    public static final int C2S_Act97_ItemsLog = 332105;//获取充值转盘的日志
    public static final int S2C_Act97_ItemsLog = 332106;

    public static final int C2S_ACTIVE_WARM_UP_BOX = 332107;//节前特惠累计积分奖励
    public static final int S2C_ACTIVE_WARM_UP_BOX = 332108;

    // 99 活动
    public static final int C2S_Act_Double_Ninth_Compose = 332125;
    public static final int S2C_Act_Double_Ninth_Compose = 332111;
    public static final int C2S_Act_Double_Ninth_Treasure = 332112;
    public static final int S2C_Act_Double_Ninth_Treasure = 332113;
    public static final int C2S_Act_Double_Ninth_TreasureLog = 332114;
    public static final int S2C_Act_Double_Ninth_TreasureLog = 332115;
    public static final int C2S_Act_Double_Ninth_Rebate = 332116;
    public static final int S2C_Act_Double_Ninth_Rebate = 332117;
    public static final int C2S_Act_Double_Ninth_HitBoss = 332118;
    public static final int S2C_Act_Double_Ninth_HitBoss = 332119;
    public static final int C2S_Act_Double_Ninth_HitBoss_BuyTimes = 332120;
    public static final int S2C_Act_Double_Ninth_HitBoss_BuyTimes = 332121;
    public static final int C2S_Act_Double_Ninth_HitBoss_Reward = 332122;
    public static final int S2C_Act_Double_Ninth_HitBoss_Reward = 332123;
    public static final int S2C_Act_Double_Ninth_TreasureLogNotify = 332124;
    // 99 活动

    public static final int C2S_ACTIVE_SECONDS_KILL = 332126;//【双十一 跨服】限时秒杀Redis数据刷新
    public static final int S2C_ACTIVE_SECONDS_KILL = 332127;
    public static final int C2S_ACTIVE_INTEGRAL_DRAW = 332128;//【双十一】积分抽奖 十连抽
    public static final int S2C_ACTIVE_INTEGRAL_DRAW = 332129;
    public static final int C2S_ACTIVE_INTEGRAL_DRAW_LOG = 332130;//【双十一】积分抽奖稀有道具日志
    public static final int S2C_ACTIVE_INTEGRAL_DRAW_LOG = 332131;
    public static final int C2S_ACTIVE_INTEGRAL_DRAW_SINGLE = 332132;//【双十一】积分抽奖 单次
    public static final int S2C_ACTIVE_INTEGRAL_DRAW_SINGLE = 332133;
    public static final int C2S_ACTIVE_SECONDS_KILL_BUY = 332134;//【双十一】金砖秒杀使用
    public static final int S2C_ACTIVE_SECONDS_KILL_BUY = 332135;

    public static final int C2S_Activity69_RechargeReward = 332136; //元旦领取七日奖励
    public static final int S2C_Activity69_RechargeReward = 332137;


    //航母

    public static final int C2S_Aircraft_StarUp = 332201; //飞机升星
    public static final int S2C_Aircraft_StarUp = 332202;
    public static final int C2S_Aircraft_LuckDraw = 332203; //抽奖
    public static final int S2C_Aircraft_LuckDraw = 332204;
    public static final int C2S_Aircraft_Decompose = 332205; //分解
    public static final int S2C_Aircraft_Decompose = 332206;
    public static final int C2S_Aircraft_AirFormation = 332207; //编队
    public static final int S2C_Aircraft_AirFormation = 332208;
    public static final int C2S_Aircraft_GetAirFormation = 332209;//获取飞机编队信息
    public static final int S2C_Aircraft_GetAirFormation = 332210;
    public static final int C2S_Aircraft_DisbandAirFormation = 332211;//解散飞机编队
    public static final int S2C_Aircraft_DisbandAirFormation = 332212;

    public static final int C2S_Aircraft_Carrier_LvUp = 332221; //航母升级
    public static final int S2C_Aircraft_Carrier_LvUp = 332222;
    public static final int C2S_Aircraft_Carrier_Build = 332223; //航母修造
    public static final int S2C_Aircraft_Carrier_Build = 332224;
    public static final int C2S_Aircraft_Carrier_Edit = 332225; //飞机上阵
    public static final int S2C_Aircraft_Carrier_Edit = 332226;


    public static final int C2S_GET_DOMAIN = 332227;// 通过 IP:port 获取域名
    public static final int S2C_GET_DOMAIN = 332228;

    public static final int C2S_SINGLE_ACTIVITY = 332229; // 通过活动id 获取活动信息
    public static final int S2C_SINGLE_ACTIVITY = 332230;

    public static final int C2S_Activity_CarrierStrike_fight = 332235;//航母出击--事件海盗
    public static final int S2C_Activity_CarrierStrike_fight  = 332236;
    public static final int C2S_Activity_CarrierStrike_move = 332241;//航母出击--出动
    public static final int S2C_Activity_CarrierStrike_move  = 332242;
    public static final int C2S_Activity_CarrierStrike_stageBoss = 332243;//航母出击--首领boss
    public static final int S2C_Activity_CarrierStrike_stageBoss = 332244;
    public static final int C2S_Activity_CarrierStrike_reset = 332247;//航母出击--重置
    public static final int S2C_Activity_CarrierStrike_reset  = 332248;
    public static final int C2S_Activity_CarrierStrike_event = 332249;//航母出击--事件
    public static final int S2C_Activity_CarrierStrike_event   = 332250;

    // 0元礼包购买
    public static final int C2S_Activity_ZeroGift_Buy = 332251;
    public static final int S2C_Activity_ZeroGift_Buy = 332252;
    // 战令
    public static final int C2S_War_Makes_Receive = 332253; // 领取奖励
    public static final int S2C_War_Makes_Receive = 332254;

    //中控系统
    public static final int C2S_Control_ElementCompose = 332261;//合成
    public static final int S2C_Control_ElementCompose = 332262;
    public static final int C2S_Control_ElementChange = 332263;//替换/上阵/卸下
    public static final int S2C_Control_ElementChange = 332264;
    public static final int C2S_Control_ElementLvUp = 332265;//升级
    public static final int S2C_Control_ElementLvUp = 332266;
    public static final int C2S_Control_ElementUpAll = 332267;//一键上阵
    public static final int S2C_Control_ElementUpAll = 332268;
    public static final int C2S_Control_ElementDownAll = 332269;//一键下阵
    public static final int S2C_Control_ElementDownAll = 332270;
    public static final int C2S_Control_Lucky = 332271;//抽奖
    public static final int S2C_Control_Lucky = 332272;
    public static final int C2S_Control_Transfer = 332273;//转移
    public static final int S2C_Control_Transfer = 332274;
    public static final int C2S_Control_ChangePos = 332275;//交换
    public static final int S2C_Control_ChangePos = 332276;
    public static final int C2S_Control_LuckyAll = 332277;//全抽
    public static final int S2C_Control_LuckyAll = 332278;


    //大军压境
    public static final int C2S_ArmyPressBorder_Open = 332301;
    public static final int S2C_ArmyPressBorder_Open = 332302;
    public static final int C2S_ArmyPressBorder_SetTroop = 332303;
    public static final int S2C_ArmyPressBorder_SetTroop = 332304;
    public static final int C2S_ArmyPressBorder_Fight = 332305;
    public static final int S2C_ArmyPressBorder_Fight = 332306;
    public static final int C2S_ArmyPressBorder_ReceiveBox = 332307;
    public static final int S2C_ArmyPressBorder_ReceiveBox = 332308;
    public static final int C2S_ArmyPressBorder_Sweep = 332309;
    public static final int S2C_ArmyPressBorder_Sweep = 332310;


    // QQ游戏蓝钻信息
    public static final int C2S_blue_vip_detail = 332315;
    public static final int S2C_blue_vip_detail = 332316;
    public static final int C2S_blue_vip_reward = 332317;
    public static final int S2C_blue_vip_reward = 332318;

    //新版中秋节活动
    // 月饼
    public static final int C2S_MidAutumn_MoonCake = 332320;
    public static final int S2C_MidAutumn_MoonCake = 332321;
    // 锦鲤红包
    public static final int C2S_MidAutumn_Red = 332322;
    public static final int S2C_MidAutumn_Red = 332323;

    public static final int C2S_MidAutumn_Play = 332324;
    public static final int S2C_MidAutumn_Play = 332325;
    // 充值后签到奖励领取
    public static final int C2S_MidAutumn_SignRecharge = 332326;
    public static final int S2C_MidAutumn_SignRecharge = 332327;

    public static final int C2S_QQ_Privilege_Reward = 332328; // QQ大厅特权 奖励领取
    public static final int S2C_QQ_Privilege_Reward = 332329;

    public static final int C2S_Aircraft_Island_Lvup = 332231; //航母-舰岛升级
    public static final int S2C_Aircraft_Island_Lvup = 332232;

    //开始游戏
    public static final int C2S_SmallGame_End = 332233;
    public static final int S2C_SmallGame_End = 332234;

    public static final int S2C_KFChatMsg = 332348;
    public static final int S2C_KFChatMsgList = 332349;

    //我请求别人添加好友
    public static final int C2S_RequestAddFriend = 332351;
    public static final int S2C_RequestAddFriend = 332352;
    //别人请求我添加好友
    public static final int S2C_RequestBeFriend = 332354;
    //回复好友申请
    public static final int C2S_RequestReplyBatch = 332355;
    public static final int S2C_RequestReplyBatch = 332356;

    //添加好友
    public static final int S2C_FriendAdd = 332358;

    //删除好友
    public static final int C2S_FriendDel = 332359;
    public static final int S2C_FriendDel = 332360;


    //私聊
    public static final int C2S_Friend_Chat = 332365;
    public static final int S2C_Friend_Chat = 332366;
    //请求好友列表
    public static final int C2S_Friend_list = 332367;
    public static final int S2C_Friend_list = 332368;

    //回复好友申请
    public static final int C2S_RequestReply = 332369;
    public static final int S2C_RequestReply = 332370;

    public static final int C2S_Activity_RichMan_Refresh = 332371; // 运输车时间结束大富翁刷新
    public static final int S2C_Activity_RichMan_Refresh = 332372;
    public static final int C2S_Activity_RichMan_Receive = 332373; //大富翁 战令一键领取礼包
    public static final int S2C_Activity_RichMan_Receive = 332374;
    public static final int C2S_Activity_RichMan_Box = 332375; //大富翁 领取礼包
    public static final int S2C_Activity_RichMan_Box = 332376;
    public static final int C2S_Activity_RichMan_BigBox = 332377; //大富翁 大礼包领取
    public static final int S2C_Activity_RichMan_BigBox = 332378;

    public static final int C2S_Activity_RedPacket = 332379; //口令红包领取
    public static final int S2C_Activity_RedPacket = 332380;

    public static final int C2S_Activity_SetMark = 332381; // 玩家选择卡包
    public static final int S2C_Activity_SetMark = 332382;

    public static final int C2S_DriverLvUp_Num = 332383; //车长升多级
    public static final int S2C_DriverLvUp_Num = 332384;

    public static final int C2S_Act117_Sign = 332385;//签到
    public static final int S2C_Act117_Sign = 332386;

    public static final int C2S_Act97_Gift = 332387;//97 活动战令礼包领取
    public static final int S2C_Act97_Gift = 332388;


    public static final int C2S_Act119_Draw = 333001;//抽奖
    public static final int S2C_Act119_Draw = 333002;
    public static final int C2S_Act119_Convert = 333005;// 碎片兑换
    public static final int S2C_Act119_Convert = 333006;
    public static final int C2S_Act119_taskProcess = 333007;// 任务进度兑换奖励
    public static final int S2C_Act119_taskProcess = 333008;

    // 121
    public static final int C2S_Act121_RechargeReward = 333009;//签到奖励领奖
    public static final int S2C_Act121_RechargeReward = 333010;
    public static final int C2S_Act121_Cycle = 333011;//充值转盘
    public static final int S2C_Act121_Cycle = 333012;
    public static final int C2S_Act121_ItemsLog = 333013;//获取充值转盘的日志
    public static final int S2C_Act121_ItemsLog = 333014;
    public static final int C2S_Act121_Gift = 333015;//97 活动战令礼包领取
    public static final int S2C_Act121_Gift = 333016;

    public static final int C2S_Activity55_Play = 333017;
    public static final int S2C_Activity55_Play = 333018;


    public static final int C2S_SHARE_REWATD = 333101; // 分享领取奖励
    public static final int S2C_SHARE_REWATD = 333102;
    public static final int C2S_SHARE_REWATD_MAIL = 333103; // 分享发送邮件奖励

    public static final int C2S_SHARE_COUNT_ADD = 333104; // 用户点击分享，添加邀请人翻牌次数

    public static final int C2S_SHARE_COUNT = 333105;
    public static final int S2C_SHARE_COUNT = 333106;

    public static final int C2S_SHARE_WX = 333107;//分享到微信
    public static final int S2C_SHARE_WX = 333108;

    public static final int C2S_Activity_Receive_All = 333019;
    public static final int S2C_Activity_Receive_All = 333020;
    public static final int C2S_Activity_Task_Reset = 333021;
    public static final int S2C_Activity_Task_Reset = 333022;
    public static final int C2S_Activity_WeekTarget_FreeGift = 333023;
    public static final int S2C_Activity_WeekTarget_FreeGift = 333024;

    public static final int C2S_Experiment_Sweep = 333025;//闪电战扫荡
    public static final int S2C_Experiment_Sweep = 333026;

    public static final int C2S_FriendGift = 333027;//好友礼物
    public static final int S2C_FriendGift = 333028;

    public static final int C2S_FriendGiftGet = 333029;//领取好友礼物
    public static final int S2C_FriendGiftGet = 333030;

    public static final int C2S_Activity_BeStronger = 333031;
    public static final int S2C_Activity_BeStronger = 333032;

    // 海军节-弹药消耗
    public static final int C2S_Activity_Set = 333033;
    public static final int S2C_Activity_Set = 333034;

    public static final int C2S_Activity_CarrierStrike_Skip = 333035;//航母出击--事件海盗
    public static final int S2C_Activity_CarrierStrike_Skip   = 333036;

    public static final int C2S_PeakBattle_Receive_All = 340413;// 巅峰挑战领取星级奖励
    public static final int S2C_PeakBattle_Receive_All = 340414;
    public static final int C2S_PeakBattle_Super_Sweep = 340415;//超级扫荡
    public static final int S2C_PeakBattle_Super_Sweep = 340416;

    // 钓鱼模块
    public static final int C2S_Fish_Record_Pre = 335007;// 钓鱼第一步
    public static final int S2C_Fish_Record_Pre  = 335008;
    public static final int C2S_Fish_Record = 335001;// 钓鱼
    public static final int S2C_Fish_Record  = 335002;
    public static final int C2S_Fish_Reward = 335003;// 钓鱼图鉴首次领奖
    public static final int S2C_Fish_Reward  = 335004;
    public static final int C2S_Fish_View_Share = 335009;// 观看他人钓鱼图鉴
    public static final int S2C_Fish_View_Share = 335010;

    public static final int C2S_Strength_Research = 334001;//力量配件研究
    public static final int S2C_Strength_Research = 334002;
    public static final int C2S_Strength_Store = 334003;//力量配件上下阵
    public static final int S2C_Strength_Store = 334004;
    public static final int C2S_Strength_Store_Sublimation = 334005;// 升华
    public static final int S2C_Strength_Store_Sublimation = 334006;
    public static final int C2S_Strength_Store_Lv = 334007;//力量配件升级
    public static final int S2C_Strength_Store_Lv = 334008;
    public static final int C2S_Strength_Store_Lock = 334009;//锁定
    public static final int S2C_Strength_Store_Lock  = 334010;
    public static final int C2S_Strength_Store_Cost = 334011;//一键 升级消耗数据
    public static final int S2C_Strength_Store_Cost = 334012;
    public static final int C2S_Strength_Store_Attr = 334013;// 精炼
    public static final int S2C_Strength_Store_Attr = 334014;
    public static final int C2S_Strength_Attr_Confirm = 334015;// 精炼 属性选择
    public static final int S2C_Strength_Attr_Confirm = 334016;
    public static final int C2S_Strength_Buy = 334017;// 购买
    public static final int S2C_Strength_Buy  = 334018;

    // 钓鱼模块
    public static final int C2S_Act2063_Record_Pre = 336001;// 钓鱼第一步
    public static final int S2C_Act2063_Record_Pre  = 336002;
    public static final int C2S_Act2063_Record = 336003;// 钓鱼
    public static final int S2C_Act2063_Record  = 336004;
    public static final int C2S_Act2063_Reward = 336005;// 钓鱼图鉴首次领奖
    public static final int S2C_Act2063_Reward  = 336006;
    public static final int C2S_Act2063_Merge = 336007;// 合成兑换奖励
    public static final int S2C_Act2063_Merge = 336008;

    // 图鉴
    public static final int C2S_TankMasterAddScore = 340001;// 领取积分
    public static final int S2C_TankMasterAddScore = 340002;
    public static final int C2S_TankMasterScoreReward = 340004;// 领取积分奖励
    public static final int S2C_TankMasterScoreReward = 340005;

    // 兽王试炼
    public static final int C2S_Player_FieldBoss_Fight = 341001;// 战斗
    public static final int S2C_Player_FieldBoss_Fight = 341002;


    //空中拦截
    public static final int C2S_KFTankLotteryDetail = 800001;//获取活动详情
    public static final int S2C_KFTankLotteryDetail = 800002;
    public static final int C2S_KFTankLotteryJoin = 800003;//架设飞机
    public static final int S2C_KFTankLotteryJoin = 800004;
    public static final int S2C_KFTankLotteryBigReward = 800005;

    public static final int C2S_Shooting_skip = 800006;//打靶跳过
    public static final int S2C_Shooting_skip = 800007;
    public static final int S2C_Login_Msg = 800008;

    public static final int C2S_DouYin_Card_Open = 800009;
    public static final int S2C_DouYin_Card_Open = 800010;

    //战役
    public static final int C2S_Player_Battle_Fight = 800101;// 战斗
    public static final int S2C_Player_Battle_Fight = 800102;
    public static final int C2S_Player_Battle_DayReward = 800103;// 领取每日奖励
    public static final int S2C_Player_Battle_DayReward = 800104;
    public static final int C2S_Player_Battle_Sweep = 800105;// 扫荡
    public static final int S2C_Player_Battle_Sweep = 800106;

    // 驭魂之地
    public static final int C2S_Player_Battle_Tower_Random_Buff = 800107;// 随机加成buff
    public static final int S2C_Player_Battle_Tower_Random_Buff = 800108;
    public static final int C2S_Player_Battle_Tower_Add_Buff = 800109;// 手动选择加成buff
    public static final int S2C_Player_Battle_Tower_Add_Buff = 800110;
    public static final int C2S_Player_Battle_Tower_PreSet_Buff = 800111;// 预设加成buff
    public static final int S2C_Player_Battle_Tower_PreSet_Buff = 800112;
    public static final int C2S_Player_Battle_Tower_Buff_Auto = 800113;// 启用预设设置
    public static final int S2C_Player_Battle_Tower_Buff_Auto = 800114;
    public static final int C2S_Player_Battle_Tower_AddAll_Buff = 800115;// 一键选择加成buff
    public static final int S2C_Player_Battle_Tower_AddAll_Buff = 800116;

}
