package com.hm.enums;
//131, 140, 347, 348, 349, 350, 352, 336, 337, 304, 305, 295, 296, 297, 298, 181, 182
public class LogType {
	public static final LogType None = new LogType("未找到", -1);
    public static final LogType Advance = new LogType("预扣", -2);
	public static final LogType Mail_Get = new LogType("邮件获取", 1);
	public static final LogType Collection = new LogType("主城收集", 5);
	public static final LogType RENAME = new LogType("更改昵称", 32);
	public static final LogType Bag_Used = new LogType("背包消耗", 34);
	public static final LogType PLayer_Default = new LogType("玩家创建初始化", 39);
	public static final LogType Item_IN_LV_UP_GIFT = new LogType("升级礼包", 44);
	public static final LogType GiftPackage_Get = new LogType("礼包获取", 46);
	public static final LogType Box_Get = new LogType("宝箱获取", 52);
	public static final LogType Recharge = new LogType("充值", 70);
	public static final LogType BuyItem = new LogType("购买道具", 74);
	public static final LogType USEEXP = new LogType("使用经验丹", 92);
	public static final LogType SweepFb = new LogType("扫荡", 94);
	public static final LogType BagSell = new LogType("背包出售道具", 96);
	public static final LogType Fb = new LogType("副本", 97);
	public static final LogType Fb_Rest_Count = new LogType("购买刷新攻打副本次数", 98);
	public static final LogType VipGift = new LogType("vip礼包", 106);
	public static final LogType TaskReward = new LogType("任务奖励", 119);
	public static final LogType ActivityReward = new LogType("活动奖励", 131);
	public static final LogType Voucher = new LogType("兑换码获得", 134);
	public static final LogType SignReward = new LogType("签到奖励", 135);
	public static final LogType ReduceByGM = new LogType("GM扣除资源", 139);
	public static final LogType Activity = new LogType("活动", 140);
	public static final LogType RedPacket = new LogType("红包", 152);
	public static final LogType LevelTarget = new LogType("等级目标", 153);
	public static final LogType OnlineReward = new LogType("在线奖励", 156);
	public static final LogType DailyTaskReward = new LogType("每日任务奖励", 157);
	public static final LogType DailyTaskBox = new LogType("每日任务宝箱", 158);
	public static final LogType IosUpdateRward = new LogType("小包热更奖励", 159);
	public static final LogType MissionBox = new LogType("关卡宝箱", 160);
	public static final LogType NewPlayerGift = new LogType("新手礼包", 161);
	public static final LogType GrowUp = new LogType("成长基金", 162);
	public static final LogType FreeGift = new LogType("免费礼包", 163);
	public static final LogType MissionOffLineBox = new LogType("离线宝箱", 164);

	//------------------------------------新日志------------------------------
	public static final LogType TreasuryLevy = new LogType("后勤处征收", 170);
	public static final LogType Mission = new LogType("关卡", 171);
	public static final LogType UnlockIcon = new LogType("解锁头像",172);
	public static final LogType TankResearch = new LogType("坦克研发",173);
	public static final LogType TankTest = new LogType("坦克测试",174);
	public static final LogType TankLvUp = new LogType("坦克升级", 175);
	public static final LogType TankStarUp = new LogType("坦克升星", 176);
	public static final LogType TankUnlock = new LogType("坦克合成", 177);
	public static final LogType TankToPapar = new LogType("坦克转为图纸",178);
	public static final LogType TankJuniorSingle = new LogType("坦克普通研究单次",179);
	public static final LogType TankJuniorMul = new LogType("坦克普通研究20次",180);
	public static final LogType TankSeniorSingle = new LogType("坦克高级研究单次",181);
	public static final LogType TankSeniorMul = new LogType("坦克高级研究10次",182);
	public static final LogType Flop = new LogType("翻牌",183);
	public static final LogType TankSkillLvUp = new LogType("坦克技能升级", 184);
	public static final LogType TankFactoryRef = new LogType("科研中心刷新消耗", 185);
	public static final LogType Shop = new LogType("商店", 186);
	public static final LogType TankFactoryDrw = new LogType("科研中心绘制消耗", 187);
	public static final LogType GuildCreateCost = new LogType("部落创建的消耗", 188);
	public static final LogType DriverLvUp = new LogType("车长升级", 189);
	public static final LogType DriverSkillLvUp = new LogType("车长升级", 190);
	public static final LogType FriendLvUp = new LogType("羁绊升级", 191);
	public static final LogType ExperimentBattle = new LogType("英雄试炼", 192);
	public static final LogType ClassicBattle = new LogType("经典战役", 193);
	public static final LogType ExperimentBattleStar = new LogType("英雄试炼星级奖励", 194);
	public static final LogType GuildChangeName = new LogType("部落修改名字", 195);
	public static final LogType Battle = new LogType("战役", 196);
	public static final LogType Expedition = new LogType("远征", 197);

	public static final LogType GuildContrAdd = new LogType("部落功勋增加", 198);
	public static final LogType WorldTroopRecoverHp = new LogType("部队回血", 199);
	public static final LogType HandTroopFullHp = new LogType("部队瞬秒回血", 200);
	public static final LogType GuildMainCityMove = new LogType("部落主城迁移", 201);
	public static final LogType CarLvUp = new LogType("座驾升级", 202);
	public static final LogType CarSkillLvUp = new LogType("座驾技能升级", 203);
	public static final LogType MilitaryLvUp = new LogType("军衔升级", 204);
	public static final LogType GuildWarRewardBase = new LogType("国战基础奖励", 205);
	public static final LogType GuildWarRewardWin = new LogType("国战胜利奖励", 206);
	public static final LogType GuildWorldReward = new LogType("部落城池奖励", 207);
	public static final LogType CarModelUnlock = new LogType("解锁座驾幻化", 208);
	public static final LogType WarReward = new LogType("城战奖励", 209);
	public static final LogType PlayerLevel = new LogType("等级变化",210);

	public static final LogType CommanderRankCost = new LogType("指挥官军衔-自动购买荣誉证书，消耗",211);
	public static final LogType CommanderCarCost = new LogType("指挥官座驾-自动购买零件，消耗",212);
	public static final LogType BlitzCost = new LogType("闪电战-购买次数，消耗",213);
	public static final LogType ExpeditionCost = new LogType("远征-购买次数，消耗", 214);
	public static final LogType ClassicBattleCost = new LogType("经典战役-购买次数，消耗", 215);
	public static final LogType ExperimentBattleCost = new LogType("英雄试炼，闪电战-购买次数，消耗", 216);
	public static final LogType WarTopCost = new LogType("巅峰对决-购买次数，消耗", 217);
	public static final LogType TowerCost = new LogType("爬塔-购买次数，消耗", 218);
	public static final LogType SuperweaponCost = new LogType("铁幕超武-购买次数，消耗", 219);
	public static final LogType ArenaCost = new LogType("竞技场-购买次数，消耗", 220);
	public static final LogType TowerBattle = new LogType("使命之路", 221);
	public static final LogType CityGuide = new LogType("城市章节奖励", 222);
	public static final LogType CityOpen = new LogType("城市解放奖励", 223);
	public static final LogType HonorLineReward = new LogType("荣誉等级奖励", 224);
	public static final LogType TrumpReward = new LogType("王牌对决奖励", 225);
	public static final LogType ArenaTopRankReward = new LogType("竞技场最高排名奖励", 226);
	public static final LogType ArenaBuyTimes = new LogType("军演挑战次数", 227);
	public static final LogType SuperWeaponLv = new LogType("超武升级", 228);
	public static final LogType ArenaFight = new LogType("竞技场胜利", 229);

	public static final LogType PvpFight = new LogType("偷袭战斗", 230);
	public static final LogType AssistCity = new LogType("助攻城战奖励", 231);
	public static final LogType CityOccupy = new LogType("攻占城池", 232);
	public static final LogType DayCard = new LogType("月卡/周卡",233);
	public static final LogType GuildDonateCost = new LogType("部落捐献,消耗", 234);
	public static final LogType GuildDonateReward = new LogType("部落捐献,奖励", 235);
	public static final LogType HangUp = new LogType("挂机", 236);
	public static final LogType TradeBuyBoat = new LogType("贸易*买船", 237);
	public static final LogType GuildTecReset = new LogType("部落科技重置消耗", 238);
	public static final LogType TradeDelivery = new LogType("贸易*交货", 239);
	public static final LogType TradeAcceptOrder = new LogType("贸易*启航", 240);
	public static final LogType TradeBuilding = new LogType("贸易*航运公司", 241);
	public static final LogType TradeExchange = new LogType("贸易*兑换", 242);
	public static final LogType TradeReform = new LogType("贸易*船只升级", 243);
	public static final LogType SupplyRefresh = new LogType("护送刷新", 245);
	public static final LogType SupplyRefreshEnemy = new LogType("刷新情报", 246);
	public static final LogType SupplyRob = new LogType("补给抢劫获得", 247);
	public static final LogType FightDropItem = new LogType("战斗掉落", 248);
	public static final LogType ExpeditionReset = new LogType("远征重置", 249);
	public static final LogType GuildFirstAdd = new LogType("首次创建或者加入部落奖励", 250);
	public static final LogType LevelEventReward = new LogType("等级事件奖励", 251);
	public static final LogType MissionFailReturn = new LogType("关卡失败返还", 252);
	public static final LogType BattleFailReturn = new LogType("战役失败返还", 253);
	public static final LogType ResFailReturn = new LogType("资源预扣返还", 254);
	public static final LogType LevelEvent = new LogType("等级事件", 255);

	public static final LogType LevelTankTech = new LogType("坦克科技升级消耗", 256);
	public static final LogType CloneTroop = new LogType("克隆部队", 258);
	public static final LogType KingWorship = new LogType("总统膜拜", 261);
	public static final LogType SupplyItem = new LogType("护送奖励", 262);
	public static final LogType WarDrop = new LogType("城战掉落", 263);

	public static final LogType StrengthenEqu = new LogType("强化装备", 270);
	public static final LogType ChangeEqu = new LogType("更换装备", 271);
	public static final LogType ComposeEqu = new LogType("合成装备", 272);
	public static final LogType ComposeStone = new LogType("合成石头", 273);
	public static final LogType ChangeStone = new LogType("更换石头", 274);
	public static final LogType LevleUpStone = new LogType("升级石头", 275);
	public static final LogType DecomposeEqu = new LogType("分解装备", 276);
	public static final LogType RichManThrow = new LogType("大富翁掷色子",277);
	public static final LogType RichManVow = new LogType("大富翁祭祀",278);
	public static final LogType RichMan = new LogType("大富翁",279);
	public static final LogType GuildImpeachCost = new LogType("弹劾部落长的消耗",280);
	public static final LogType OverallWar = new LogType("全面战争",281);
	public static final LogType TrumpArena = new LogType("王牌演习*胜场奖励",282);
	public static final LogType TankEvolveStarUp = new LogType("坦克进化升星", 283);
	public static final LogType ArenaTrumpReset = new LogType("王牌军演重置", 284);
	public static final LogType KfActivity = new LogType("跨服活动", 285);
	public static final LogType RandomTask = new LogType("民情", 286);
	public static final LogType LuckCampGold = new LogType("推荐阵营奖励", 287);
	public static final LogType CampImpeach = new LogType("阵营弹劾", 288);
	public static final LogType KfSportsWorship = new LogType("跨服膜拜", 289);
	public static final LogType InviteReward = new LogType("邀请有礼", 290);
	public static final LogType BindPhone = new LogType("绑定手机", 291);
	public static final LogType ExpeditionBuyBuff = new LogType("远征购买buff", 292);
	public static final LogType ResBack = new LogType("资源找回", 293);

	public static final LogType Mastery = new LogType("专精升级", 294);
	public static final LogType ArmyDayCost = new LogType("81活动消耗资源-探索--钻石或者道具", 295);
	public static final LogType ArmyDayDevRew = new LogType("81活动-探索-奖励", 296);
	public static final LogType ArmyDayBossCost = new LogType("81活动消耗资源-boss--减cd消耗", 297);
	public static final LogType ArmyDayBossDouble = new LogType("81活动消耗资源-boss--鼓励，增加战力倍数", 298);

	public static final LogType FightEndResetCost = new LogType("【血战到底】修复单个战车消耗金砖", 299);
	public static final LogType TankSpecChangeCost = new LogType("【专精】每天切换时消耗价格（第一次免费）", 300);
	public static final LogType TankLvCost = new LogType("【专精】每天切换时消耗价格（第一次免费）", 301);
	public static final LogType Agent = new LogType("特工", 302);
	public static final LogType FightEndFirstCost = new LogType("血战到底，每天第一次消耗", 303);
	public static final LogType MidAutumnMerge = new LogType("中秋节活动--合成月饼消耗", 304);
	public static final LogType MidAutumnReward = new LogType("中秋节活动--获取月饼奖励", 305);
	public static final LogType MidAutumnResource = new LogType("中秋节活动--获取资源礼包奖励", 305);
	public static final LogType WorldBoss = new LogType("世界boss", 306);
	public static final LogType WorldBossHurt = new LogType("世界boss伤害", 307);
	public static final LogType KfExpeditionDeclare = new LogType("跨服远征宣战", 308);
	public static final LogType KfBuyOil = new LogType("跨服购买石油", 309);
	public static final LogType WorldBossBox = new LogType("世界boss箱子", 310);
	public static final LogType MilitaryProjectCost = new LogType("指挥官--军工升级消耗", 311);
	public static final LogType PresidentExp = new LogType("总统特权经验", 312);
	public static final LogType TankRecast = new LogType("坦克重铸", 313);
	public static final LogType MedalLvUp = new LogType("勋章升级", 314);
	public static final LogType MedalDayReward = new LogType("勋章每日奖励", 315);
	public static final LogType Passenger_Compose = new LogType("合成乘员", 316);
	public static final LogType Passenger_LvUp = new LogType("乘员升级", 317);
	public static final LogType Passenger_StarUp = new LogType("乘员升星", 318);
	public static final LogType Passenger_Culture = new LogType("乘员培养", 319);
	public static final LogType MilitaryLineUp = new LogType("军阵", 320);
	public static final LogType Passenger_Retire = new LogType("乘员退役", 321);
	public static final LogType FarWin_ReceiveDay = new LogType("决胜千里每日奖励", 322);
	public static final LogType FarWin_MileReward = new LogType("决胜千里里程奖励", 323);
	public static final LogType RaidBattle = new LogType("单兵奇袭", 324);
	public static final LogType GuildTaskReward = new LogType("部落任务奖励", 325);
	public static final LogType TankMagicReform = new LogType("坦克魔改", 326);
	public static final LogType WorldBuildDayReward = new LogType("世界建筑每日奖励", 327);
	public static final LogType WorldBuildTaskReward = new LogType("世界建筑任务奖励", 328);
	public static final LogType SkipGuide = new LogType("跳过新手引导", 329);
	public static final LogType MysteryShop = new LogType("神秘商店", 330);
	public static final LogType TankMagicReformTransfer = new LogType("坦克魔改转移", 331);
	public static final LogType ActivityTask = new LogType("活动任务", 332);
	public static final LogType ActivityShop = new LogType("活动商店", 333);
	public static final LogType ActivitySoldierBuyShop = new LogType("老兵回归商店", 334);
	public static final LogType ActivitySoldierKfReward = new LogType("老兵回归--跨服奖励", 335);
	public static final LogType ActivityDiscountCost = new LogType("抽折扣活动-购买消耗", 336);
	public static final LogType ActivityDiscountRandomCost = new LogType("抽折扣活动-抽奖消耗", 337);
	public static final LogType ActivitySoldierCostReward = new LogType("老兵回归消耗获取奖励", 338);
	public static final LogType Activity69Cost = new LogType("元旦狂欢-购买游戏币消耗", 339);
	public static final LogType Activity69UnlockCost = new LogType("元旦狂欢-解锁消耗", 339);
	public static final LogType Activity69Reward = new LogType("元旦狂欢-抽奖", 340);
	public static final LogType Activity69CostCoin = new LogType("元旦狂欢-游戏币消耗", 341);
	public static final LogType Activity69DisableCost = new LogType("元旦狂欢-禁用格子消耗", 342);
	public static final LogType Activity69BuyCoin = new LogType("元旦狂欢-购买游戏币", 343);
	public static final LogType Activity75Reward = new LogType("春节7天乐，每日完成任务奖励", 344);
	public static final LogType Activity75GameReward = new LogType("春节7天乐，游戏奖励", 345);
	public static final LogType Activity75Buy = new LogType("春节7天乐，购买获取", 346);

	public static final LogType LanternReward = new LogType("元宵节活动--获取月饼奖励", 347);
	public static final LogType LanternMerge = new LogType("元宵节活动--合成月饼消耗", 348);
	public static final LogType LanternResource = new LogType("元宵节活动--获取资源礼包奖励", 349);
	public static final LogType LanternBuyCost = new LogType("元宵节活动--金砖礼包消耗", 350);
	public static final LogType LanternBuyGet = new LogType("元宵节活动--金砖礼包获取", 352);

	public static final LogType Equ_LvUp = new LogType("装备-升级阶", 353);
	public static final LogType QueueSpeed = new LogType("队列-加速", 354);
	public static final LogType BuildLvUp = new LogType("建筑-升级", 355);
	public static final LogType Queue_Cancel = new LogType("队列-取消", 356);
	public static final LogType Queue_Collection = new LogType("队列-收取", 357);
	public static final LogType Queue_Produce = new LogType("队列-生产", 358);
	public static final LogType Queue_Buy = new LogType("队列-购买", 359);
	public static final LogType PhotoWallLv = new LogType("照片墙", 360);
	public static final LogType PhotoChapterLvUp = new LogType("照片墙升级", 361);
	public static final LogType PhotoRecovery = new LogType("照片回收", 362);
	public static final LogType Activity75GameCost = new LogType("春节7天乐-游戏消耗", 363);
	public static final LogType Res_Collect = new LogType("资源收取", 364);
	public static final LogType ChSmShopReward = new LogType("草花线下奖励", 365);
	public static final LogType Tank_Strength = new LogType("坦克强化", 366);

	public static final LogType WarCraft_lvUpdate = new LogType("兵法升级消耗", 367);
	public static final LogType WarCraft_SkillLvUpdate = new LogType("兵法技能升级消耗", 368);
	public static final LogType TankTrain = new LogType("坦克训练", 369);
	public static final LogType BuildRob = new LogType("征讨抢劫", 370);
	public static final LogType BuildInitRes = new LogType("基地解锁初始资源",371);
	public static final LogType Strength = new LogType("力量系统",414);


	public static final LogType TankDriverQuit = new LogType("坦克车长军职退出",372);
	public static final LogType TankDriverLvup = new LogType("坦克车长军职升级",373);
	public static final LogType SystemClean = new LogType("系统清除",374);

	public static final LogType ArmyRaceTenderCost = new LogType("军备竞赛-生成订单标书消耗",375);
	public static final LogType ArmyRaceOrder = new LogType("军备竞赛-生成订单",376);

	public static final LogType GuildBuild = new LogType("部落军工厂-建设",377);
	public static final LogType GuildProduce = new LogType("部落军工厂-生产(提交证书)",378);
	public static final LogType BuyArmsPos = new LogType("部落军工厂-购买武器槽位",379);
	public static final LogType RepairTrain = new LogType("维修训练",380);
	public static final LogType ArmsDecompose = new LogType("武器分解",381);

	public static final LogType SpecialMedalMerge = new LogType("特殊勋章合成",382);
	public static final LogType SpecialMedalUpStar = new LogType("特殊勋章升星",383);
	public static final LogType SpecialMedalUpLv = new LogType("特殊勋章升级",384);

	public static final LogType CityWarSkillReward = new LogType("偷袭突进撤退任务奖励",385);
	public static final LogType CityWarSkillCost = new LogType("城战技能升级消耗",386);
	public static final LogType TankSoldierLvUp = new LogType("坦克奇兵升级",387);
	public static final LogType TankJingZhuRandomCost = new LogType("坦克精铸，随机消耗",388);
	public static final LogType CampConvert = new LogType("阵营转换", 389);
	public static final LogType CaptiveLvUpCost = new LogType("战俘营升级消耗",390);
	public static final LogType CaptiveBuyResearcherCost = new LogType("战俘营购买研究员消耗",391);
	public static final LogType CaptiveResearcherReward = new LogType("战俘营研究获得",392);
	public static final LogType BerlinBoss = new LogType("首都boss",393);
	public static final LogType CaptiveRedeemCost = new LogType("战俘营赎回坦克消耗",394);
	public static final LogType AutoDriveLvUp = new LogType("自动驾驶升级",395);
	public static final LogType CarModelStarUp = new LogType("座驾皮肤升级",396);

	public static final LogType ArmsProduce = new LogType("武器生产",397);

	public static final LogType AircraftLuckDraw = new LogType("飞机抽奖",398);
	public static final LogType AircraftDecompose = new LogType("飞机分解",399);
    public static final LogType AircraftCarrierLvUp = new LogType("航母升级",400);
	public static final LogType AircraftCarrierEngineLvUp = new LogType("航母动力舱升级",401);
	public static final LogType LevelEvent_CallSupport = new LogType("等级事件呼叫支援", 402);
	public static final LogType LevelEventRewardNew = new LogType("等级事件呼叫支援", 403);
	public static final LogType LevelEventNew = new LogType("等级事件", 404);
	public static final LogType Viedo = new LogType("直播", 405);
	public static final LogType WarMakes = new LogType("战令", 406);
    public static final LogType KFTask = new LogType("跨服任务", 407);


	//元件

	public static final LogType ElementResearch = new LogType("研究元件", 408);
	public static final LogType Controltransfer = new LogType("中控转移", 409);
    public static final LogType ComposeElement = new LogType("合成元件", 410);
	public static final LogType ChangeElement = new LogType("更换元件", 411);
	public static final LogType lvUpElement = new LogType("元件升级", 412);

	//航母-舰岛
	public static final LogType AircraftIslandLvup = new LogType("航母舰岛升级", 413);


    //世界大战
    public static final LogType Gather = new LogType("世界大战:采集", 501);
    public static final LogType KfCityBuild = new LogType("世界大战:重建", 502);
    public static final LogType KfChangeMainCity = new LogType("世界大战:改变主城", 503);
    public static final LogType KfSecondCityBuild = new LogType("世界大战:修建分城", 504);
	public static final LogType KfWorld_Task = new LogType("世界大战:任务", 505);
	public static final LogType KfWorld_Produce = new LogType("世界大战:主城产出", 506);
	public static final LogType KfWorld_SuperWeaponLvUp = new LogType("世界大战:超武升级", 507);
    public static final LogType Blue_vip = new LogType("蓝钻礼包奖励", 508);
	public static final LogType SmallGame = new LogType("小游戏奖励", 509);

	public static final LogType Red_Packet = new LogType("口令红包奖励", 510);
	public static final LogType AgentCenterLvUp = new LogType("特工中心升级", 511);
    public static final LogType Big_Sys_Gold = new LogType("大额系统砖获取（财神投资奖励）", 512);

    public static final LogType SHARE_REWARD = new LogType("分享奖励",513);
	public static final LogType RankReward = new LogType("排行奖励",514);
	public static final LogType FriendGift = new LogType("好友赠送",515);
	public static final LogType AdReward = new LogType("看广告",516);
	public static final LogType AgentDispatch = new LogType("特工派遣",517);
	public static final LogType BattleReward = new LogType("战役记录奖励", 518);
	public static final LogType TankMagicReformReset = new LogType("坦克魔改重置", 519);
	public static final LogType Fish = new LogType("钓鱼", 520);
	public static final LogType ShareReward = new LogType("分享",521);
	public static final LogType TankReform = new LogType("tank突破",522);
	public static final LogType TankMaster = new LogType("图鉴大师",523);
	public static final LogType FieldBoss = new LogType("兽王试炼",524);
	public static final LogType BattleDayReward = new LogType("战役每日奖励",525);
	public static final LogType BattleSweepReward = new LogType("战役扫荡奖励",526);
	public static final LogType DriverEvolveStarUp = new LogType("兽魂觉醒", 527);


	private int code;
	private String desc;
	private String extra;

	public LogType(String desc, int code) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getExtra() {
		return extra;
	}

	public LogType value(String extra) {
		LogType clone = new LogType(this.desc, this.code);
		clone.extra = extra;
		return clone;
	}
	public LogType value(long extra) {
		return value(extra+"");
	}
}
