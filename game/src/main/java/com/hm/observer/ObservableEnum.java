/**
 * Project Name:SLG_Game.  
 * File Name:ObservableEnum.java  
 * Package Name:com.hm.observer  
 * Date:2017年9月19日上午10:15:31  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *
 */

package com.hm.observer;

/**
 *  可被观察的信号枚举
 * ClassName:ObservableEnum <br/>  
 * Date:     2017年9月19日 上午10:15:31 <br/>  
 * @author   zigm
 * @version 1.1
 * @since
 */
public enum ObservableEnum {
    LOGIN(1) {//登陆

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    BuildLevelUpStart(2) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    PlayerGuideChange(3) {//新手引导

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    VipLevelUp(5) {//vip升级

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    PlayerLevelChange(8) {//玩家等级变化,每一等级都会触发

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PlayerLevelUp(9) {//玩家等级变化,如果连续升2级只会触发一次

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    ChangeName(22) {//改变昵称
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MaxCombatChange(23) {//最强战斗力改变
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TroopCombatChange(24) {//部队战力发生改变
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TroopChange(25) {//部队发生变化(创建部队，编辑部队，解散部队)

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    PlayerCreate(26) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TankAdd(30) {//招募坦克

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankLv(31) {//坦克升级

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankExp(32){//坦克加经验，不升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankStarUp(33){//坦克升星
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankPartsCompose(34){//坦克配件合成
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankReform(35){//坦克改造
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankSkill(36){//坦克技能升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankListLv(37){//多个坦克升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankListExp(38){//多个坦克加经验
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankListState(39){//多个坦克状态变化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    WarGodLv(40){//战神升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    DriverLv(40){//车长升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    DriverSkillLv1(41){//车长技能升级，只影响自己的属性
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    DriverSkillLv2(42){//车长升级，影响全体坦卡属性
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    DriverSkillLv3(43){//车长升级，场内属性
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    FriendLv(44){//羁绊升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CarLv(45){//座驾升级或者升格
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MilitaryLvChange(46){//参数1 军衔是否升级 参数2 是否军衔升级或升格
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CarModelUnLock(47){//解锁座驾幻化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CarModelIcon(48){//设置座驾头像
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TaskComplete(49){//完成任务
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MainTaskComplete(50){//完成主线任务
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KFRankFight(51),//角斗场
    ChangeIcon(54){//改头像
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    ChangeFrameIcon(55){//改头像框
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TankCombatChange(56){//坦克战力变化
        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    NotChat(58){//禁言
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MilitaryLvUp(59),//军衔升级

    ShopBuy(61){//购买商店物品
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    Recharge(62){//充值
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CostRes(63){//使用资源
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    AddRes(64){//增加资源
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    RecaptureCity(65){//收复城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    UnlockIcon(66){//解锁头像
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AddTankPaper(67){//增加坦克图纸
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AddPlayerExp(68){//增加玩家经验
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CityBattleResult(69) {//战斗结束后,杀敌数和损失数
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 7;
        }
    },
    Pvp1v1Result(70) {//单挑结束
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PlayerCityOccupy(71) {//成功攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    AddBagItem(72){//增加背包道具
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AddHonor(73){//荣誉
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    TankTest(74) {//坦克测试
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Expedition(75) {//远征
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    EliminateBandits(76) {//剿匪(军械库挑战或运输)
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TreasuryCollect(77) {//府库征收
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    BattleFight(78) {//战役战斗
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    TankTechResearch(79) { //tank 科技中心研发 十连抽
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankResearch(80) { //tank驯化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    GuildWarReward(82) {//领取国战奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    FunctionUnlock(83) {//解锁功能
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    SuperWeaponLv(84){//超武升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    ArenaFight(85){//竞技场挑战
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    DeclareWar(86){//宣战
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildWarHerald(87),
    TankHpUpdate(88){//坦克血量发生变化(世界不对解散，编辑后同步血量到坦克列表)
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AddPiece(89){//增加装备碎片
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AddEquip(90){//增加装备
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    HourEvent(91){//整点事件,0点重置玩家数据前发出
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ClearnceMission(92){//通关关卡
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    HangUpEnd(93){//挂机结束
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    OccupyCity(94){//攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    ChangeTitle(95){//改变称号
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    FightMission(96){//打赢关卡
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    GiveUpCity(97){//放弃城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MissionBox(98),
    MissionWave(99) ,//打完一波

    //广播--停服、关服
    SYSSS(100) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    //广播--特殊事件
    SYSTS(101) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    //广播--活动开启、活动结束提示
    SYSACTITITY(102) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    //广播--活动开启、活动结束提示
    USRESPEAKER(103) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    //玩家行为--vip等级
    USERVIP(107) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildCreate(108) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildChangeNameOrFlag(110) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildPlayerAdd(111) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildPlayerQuit(112) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildDel(113) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    DailyTaskComplete(114) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ItemNotEnough(115),//道具不足

    TradeBegin(117) {//贸易启航
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TradeExchange(118) {//贸易兑换
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    GuildUpdateLv(119) {//部落升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankTechUpdate(120) {//坦克科技升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    TankDraw(121) {//坦克图纸绘制
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildChangeJob(122) {//部落升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },

    ComposeEqu(130){//改变装备（装备，卸下，更换）
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    StrengthenEqu(131){//强化装备
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length <= 1;
        }
    },
    RefineEqu(132){//精炼装备
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ChangeStone(133),
    StoneLvUp(134){//升级石头
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildChangeMainCity(135){//迁城事件
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    GoldStatistics(136){//金币购入和消耗
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    GoldDaySurplus(137){//金币日剩余
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 6;
        }
    },
    Vow(138){//踏青活动祭祀
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ServerFunction(139){//服务器功能开启
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankEvolveStarUp(140){//坦克进化升星
        @Override
        public boolean chkArgv(Object... argv) {
            // 坦克id 进化后坦克星级 消耗道具id 消耗道具数量 剩余道具数量
            return argv.length == 3;
        }
    },
    TrumpFight(141){//王牌演习战斗
        @Override
        public boolean chkArgv(Object... argv) {
            // argv[0], boolean,isWin:true
            return argv.length == 1;
        }
    },
    CampOfficialChange(142){//阵营职位变动
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    JoinCamp(143){//加入阵营
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    UseTactics(144){//使用战术
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CampOfficialLogin(145){//阵营官员登录
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PlayerLoginSuc(146){//登录成功
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    PveRebelOccupyCity(147){//首日PVE攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PveRebelSuc(148){//首日PVE攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PveRebelFail(149){//首日PVE攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TroopRetreat(150){//部队撤退
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TroopAdvance(151){//部队突进
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AssistCity(152){//助攻
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KillGhostBoss(153){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    CloneTroop(154){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    BerlinReduce1Hp(155){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    FundGroupon(156) { //基金团购，param1:point
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    BattleBuyCount(157){//战役购买次数
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    BattleReset(158){//战役重置
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ExpeditionReset(159){//远征重置
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KfManorOcc(160){//远征重置
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    RandomTaskComplete(161) { //完成民情事件
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AnswerQuestion(162) { //参与一次答题
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    SupplyTroop(163) { //补给部队
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    GuildDonation(164) { // 部落捐献
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MasteryLvUp(165){//专精升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankSpecialLvUp(166){//坦克--专精升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    TankSpecialChange(167){//坦克--专精切换
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Agent(168) { //特工 argv[0]:agent id
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },

    MailGetReward(169) { //邮件---领取奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MailReadAll(170) { //邮件---一键领取奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    FBFightWin(171) { //兵工厂---战斗胜利
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    WorldBossKill(172){//世界boss击杀
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    WorldBossStartHot(173){//世界boss活动开始5分钟预热
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    WorldBossStart(174){//世界boss活动开始
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CloneTroopDeath(175) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    //指挥官--军工升级
    MilitaryProjectLvUp(176) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    FindBlackMainCity(177) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TroopRevive(178) {//部队从死亡到复活
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },

    KfScoreStart5(179),
    KfScoreStart(180),
    KfExpeditionOccAtk(181) {//跨服远征
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KfExpeditionOccDef(182) {//跨服远征
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KfExpeditionOccWin(183) {//跨服远征
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KfExpeditionOccFail(184) {//跨服远征
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },

    ArenaBuyTimes(185) {//军演购买
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MilitaryPolicy(186) {//总统颁布军事政策
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    OccBlackMainCity(187) {//攻下黑鹰主城
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Power_CenterCity(188) {//设置主城
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Power_Policy(189) {//颁布军事政策
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Power_Punish(190) {//制裁
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Passenger_Eq(191) {//乘员上阵/下阵
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    Passenger_LvUp(192) {//乘员升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    Passenger_StarUp(193) {//乘员升星
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    Passenger_Culture(192) {//乘员培养
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    TankRecast(193) {//坦克重铸
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MilitaryLineup(194) { //军阵
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MedalIdChange(195) {//勋章更换
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    MedalValueUp(196) {//勋章价值提升
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MinuteEvent(197) {//现在只有30分时候的事件!!!!
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MedalMaxLvUp(198) {//勋章等级提升
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    MagicReform(199) {//魔改
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 6;
        }
    },
    OpenDailyTaskBox(200) {//开每日宝箱
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    EggGetTrueItem(201) {//获取实物奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    SuperWeaponUpgrade(202){//超武升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AddPassenger(203){//增加乘员
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AddPassengerPiece(204){//增加乘员碎片
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AddStone(205){//增加宝石
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    WorldBuildTaskRelease(206){//世界建筑任务发布
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    WorldBuildLvChange(207){//世界建筑任务发布
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CampMainCityChange(208){//阵营主城变化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ArsenalSweep(209){//扫荡兵工厂
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MagicReformTransfer(210){//魔改转移
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    SantaClausForecast(211){//圣诞老人预告
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    SantaClausAppear(212){//圣诞老人出现
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    SantaClausBigGift(213){//圣诞老人大奖
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GuildScoreAdd(214){//部落任务积分活动
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    FightPveBoss(215) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },//兽王试炼
    OverallWar(216){//全面战争
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    ActivityShotting(217){//坦克打靶
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length <= 1;
        }
    },
    //参数1:战役类型 2：关卡id  3:次数
    PartakeBattle(218){//参与战役
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length >= 2;
        }
    },
    WorldBuildOpen(219){//世界建筑开启
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    YearGetItems(220) {//获取实物奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    KFScoreMainCityLose(221) {//跨服积分战主城摧毁
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    BindIdCode(222) {//绑定唯一码
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    EquLvUp(223){//升级装备
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ProduceCollection(224){//生产收取
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ProduceStart(225){//生产开始
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    BuildLvUp(226){//建筑升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ActivityTaskComplate(227) {//活动任务完成事件
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MemorialHallChapterLv(228) {//章节升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MemorialHallChapterPhotoAdd(229) {//章节照片添加
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    SYSTSBETWEENTIME(230) {//时间段广播
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    SpringActivityBagEnd(231) { //春节活动特定时间通知
        @Override
        public boolean chkArgv(Object... argv) {
            // server id
            return argv.length == 1;
        }
    },
    SpringActivityBagReward(232) { //春节活动特定时间通知
        @Override
        public boolean chkArgv(Object... argv) {
            // server id
            return argv.length == 1;
        }
    },
    SpringActivityKillBoss(233) { //春节活动打死年兽
        @Override
        public boolean chkArgv(Object... argv) {
            //  boss id
            return argv.length == 1;
        }
    },
    MailReward(234) { //邮件奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    ChSmShopReward(235) { //草花获取线下奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    GetSysGold(236) { //获取系统金砖
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankDrawFre(237) { //坦克绘制刷新
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Task_Reward(238) { //奖励领取
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    Treasury_Collection(239) { //后勤部购买次数
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TankStrength(240) { //坦克强化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MainFbFail(241) { //主关卡失败
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    PvpOneByOneLaunch(242) { //偷袭发起
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },

    WarCraftLvup(243) { //兵法升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    WarCraftSkillLvup(244) { //兵法节能升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 6;
        }
    },
    TankStrengthBreach(245) { //坦克强化突破
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankStrengthTaskChange(246) { //坦克强化成就发生变化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    TankDriverAdChange(247){//坦克车长，军职等级变化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankDriverAdChangeFetter(248){//坦克车长，军职等级，羁绊等级变化
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ChangePlayerSet(249){//改变玩家设置
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PlayerHeart(250){//玩家心跳,1分钟发一次
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AllServerBroad(251){//全服广播
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    RechargeSdk(252){//用于sdk充值信息
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    OneDollarPrize(253) { //一元夺宝开奖广播
        @Override
        public boolean chkArgv(Object... argv) {
            // serverId(int), playerName(String), prize(String)
            return argv.length == 3;
        }
    },
    WeekPointAdd(254) { //活跃积分,任务活跃积分
        @Override
        public boolean chkArgv(Object... argv) {
            // serverId(int), playerName(String), prize(String)
            return argv.length == 1;
        }
    },
    OneDollarAllIn(255) { //一元夺宝买断
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    PlayerEquipChange(256) { //玩家装备变更
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ArmsChange(257) { //武器变化(上阵，下阵，更换)
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },

    KFWzStart(259) { //王者开始
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KFWzCenter(260) { //王者开始
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    SpecialMedalStarOrLvUp(261) { //特殊勋章升星或升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length >= 0;
        }
    },
    TankCaptive(262) { //俘虏坦克
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    SoldierBind(263) { //绑定奇兵
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    SoldierUnBind(264) { //解绑奇兵
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    SoldierLvUp(265){//奇兵升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ChangeArenaTroop(266) { //改变竞技场部队
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankJingzhuAttrChange(267){//坦克--精铸属性改变
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TankJingzhuLvup(268){//坦克--精铸，升星
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    OpenBerlinBoss(269){//开启最后的战役
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    BerlinWarOpen(270){//柏林战开启
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AutoDriveLvUp(271){//自动驾驶升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    CarModelStarUp(272){//座驾升星
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KfBuildHunterFind(273){//找到猎人广播
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    Act97GetItems(274) {//获取实物奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    BattleSweep(275) {//战役扫荡
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    DoubleNinthActivityKillBoss(276) { //99活动打死年兽
        @Override
        public boolean chkArgv(Object... argv) {
            //  boss id
            return argv.length == 1;
        }
    },
    KfScuffleOccupyMine(281) { //跨服乱斗占领核晶矿
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    KfScuffleAirportReset(282) { //跨服乱斗机场刷新
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KfScuffleAirportBossKill(283) { //跨服乱斗机场BOSS被击杀
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KfScuffleStart(284) { //跨服乱斗开始
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AircraftAdd(285){//增加飞机
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AircraftStarUp(286){//飞机升星
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    AircraftCarrierLvUp(287){//航母升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AircraftCarrierBuild(288){//航母修造
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    KFActOpen(289) { //跨服活动开启
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AircraftCarrierEit(290){//航母上阵飞机
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    Activity202GetItems(291) {// 活动二月二  获取实物奖励
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    AddAircraftCarrier(292){//获得飞机
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    ChangeElement(293){//改变元件(装备，卸下，更换)
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ElementLvUp(294){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    ControlTransfer(295){//中控转移
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    AddElement(296) {//获得元件
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    ActivePointAdd(297) {
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Sign(298){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    ShareWx(299){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TradeCompanyLvUp(300) {//公司升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },

    StrengthLvChange(301){//力量碎片升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },

    StrengthSublimationChange(302){//力量碎片升华
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    StrengthAttrChange(303){//力量碎片精炼
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    StrengthChange(304){//力量碎片上下阵
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },


    playerJoinOccupyCity(401){ //玩家参与 攻占城池
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ClonePlayerTroopWin(402){ //玩家镜像部队 杀死敌人
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    MagicReformRest(403){//魔改重置
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },

    FishRecord(306){//钓鱼
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    Act2063Record(308){//钓鱼活动
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },

    KFScoreGNpcPre5(309),//跨服积分站野蛮人前5分钟预告
    KFScoreGNpcOut(310),//跨服积分站野蛮人出现
    KFScoreScoreChangePre5(311),//跨服积分变化5
    KFScoreScoreChange(312),//跨服积分变化


    TankLotteryBigReward(1001) {//空中拦截大奖产生

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },

    PlayerLoginOut(1029){//用户退出
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    PlayerLoginZero(1030){//用户凌晨登录，游戏服自动模拟用户的一次登录
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AircraftIslandLvup(1031){//航母-舰岛-升级
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    BindPhone(1032){//绑定手机号
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    PlayerReLogin(1033){//用户退出
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ItemChange(1034){//item改变
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    TankAddExp(1035){//坦克加经验
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHTankSkillLVUp(1036) { //坦克技能升级,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid, skillid,lvbefore,lvafter,cashcost
            return argv.length == 5;
        }
    },
    CHTankPartsCompose(1037) { //坦克改造-配件合成,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid, pos,当前配件碎片数量,改变后配件碎片数量
            return argv.length == 4;
        }
    },
    CHTankRecast(1038) { //坦克改造-重铸,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid,cost item id, remain cost item id
            return argv.length == 4;
        }
    },
    CHTankJingzhuLvup(1040) { //坦克精铸-配件升级,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid, position,分解前配件等级,分解后配件等级,partsIds,消耗配件碎片数量,分解后精铸经验
            return argv.length == 7;
        }
    },
    CHTankJingzhu(1041) { //坦克精铸-配件精铸,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid, position,当前精铸属性,分解后配件等级,partsIds,消耗配件碎片数量,分解后精铸经验
            return argv.length == 7;
        }
    },
    CHTankDriverLvUp(1042) { //坦克车长-车长等级,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // 坦克车长id 当前车长等级 改变后车长等级 消耗道具id 消耗道具数量 剩余道具数量
            return argv.length == 4;
        }
    },
    CHTankDriverSkillLvUp(1043) { //坦克车长-车长技能等级,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            //坦克id 车长技能位置id 变更强化等级 消耗道具id 消耗道具数量 剩余道具数量
            return argv.length == 6;
        }
    },
    CHTankDriverAdvanceLvUp(1044) { //坦克车长-车长军职,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            //坦克id 变更后军职id 升职所选服役部队和属性 消耗道具id 消耗道具数量 剩余道具数量
            return argv.length == 4;
        }
    },
    CHTankCtrlChange(1045) { //坦克中控-原件装配,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            //坦克id 中控原件位置 当前装配原件id 改变后装配原件id
            return argv.length == 4;
        }
    },
    CHPassengerRetire(1046) { //坦克乘员退役,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // 退役乘员id
            return argv.length == 1;
        }
    },
    CHPassengerUp(1047) { //坦克乘员,统计用
        @Override
        public boolean chkArgv(Object... argv) {
            // tankid 乘员ids
            return argv.length == 2;
        }
    },

    ExpeditionEvent(1048) {//远征参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    TowerBattleEvent(1049) {// 使命之路 普通扫荡

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    RecoveryBattle(1050) {// 战场寻宝

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    FarWinBattleReward(1051) {// 决胜千里 每日领奖

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    FrontBattleEvent(1052) {// 前线战争参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    FrontBattleShare(1053) {// 前线战争邀请

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    RareTreasureBattle(1054) {// 秘境宝藏参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    RareTreasureFight(1055) {// 秘境宝藏战斗

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    FightEndEvent(1056) {// 血战到底

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    FightEndRepairEvent(1057) {// 血战到底修复战车

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    RaidBattleEvent(1058) {// 单兵奇袭参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    ModernBattleEvent(1059) {// 现代战争

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    BattleSweepEvent(1060) {// 扫荡

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length >= 2;
        }
    },
    PeakBattleEvent(1061) {// 巅峰挑战参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    DreamlandBattleEvent(1062) {// 梦回沙场

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    ArmyPressBorderBattleEvent(1063) {// 大军压境

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    BattleEvent(1064) {//战役参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    BattleResetEvent(1065) {// 重置购买参与

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },

    CHAgentActive(1066) { //草花打点统计用
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CHAgentTraining(1067) { //草花打点统计用
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHAgentSkill(1068) { //草花打点统计用
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    CHAgentReward(1069) { //草花打点统计用
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AircraftCarrierEitEvent(1070) {//航母上阵飞机

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    AircraftDecomposeEvent(1071) {//指挥官航母飞机拆解

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    AircraftCarrierLvUpEvent(1072) {//航母升级

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    MedalMaxLvUpEvent(1073) {//指挥官勋章升级

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },


    CHCommancerLvUpEvent(1074){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHCommancerDriverLvUpEvent(1075){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHCommancerCarSikllLvUp(1076){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHCommancerUnlockCarModel(1077){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CHCommancerCarModelStar(1078){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHCommancerAutoDriveLvUp(1079){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHCommancerSetCarIcon(1080){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CHEquChange(1081){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHEquCompose(1082){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CHEquStreng(1083){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHEquChangeStone(1084){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHEquStoneCompose(1085){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    CHEquStoneDeCompose(1086){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CHEquMilitaryProjectLvUp(1087){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHEquWeaponLv(1088){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    //////////////////
    CHMasteryLvUp(1089){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 5;
        }
    },
    CHMilitaryLineup(1090){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    MilitaryAssistanceLvup(1091){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    MilitaryLineLvUp(1092){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHMemorialChapterLv(1093){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    CHShowPhoto(1094){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    CHRefine(1095){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    PlayerTroopBerlin(1096){//玩家部队进到柏林城
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TroopEnterCity(1097){//部队进到城里面
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    KfKingSignup(1098){//跨服王者峡谷，报名
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KfScuffleSignup(1100){//跨服极地乱斗，报名
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    AgentCenterLvUp(1101) {//特工中心升级

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    HonorReceive(1102){
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TroopRepairNow(1103) {//立即修复部队
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },
    TankDrawShuffleLucky(1104) { //触发幸运降临的机会
        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    DayFirstLogin(2002) {//每天第一次登录

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    AdShow(2003) {
        @Override
        public boolean chkArgv(Object... argv) {
            // ad id
            return argv.length == 1;
        }
    },

    CHAct4003(6001) {//草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4006(6002) {//草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4005(6003) {// 草花活动统计  档位选择

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHRankStat(6004) {//草花排行统计

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHKFPlayerJoin(6005) {//草花排行统计

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CHKFPlayerOil(6006) {//草花排行统计

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    CHKFPlayerKFResult(6007) {//草花排行统计

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    CHHonorStat(6008) {//草花荣誉统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4007(6009) {// 草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4011(6010) {// 草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4012(6011) {// 草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4013(6012) {// 草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHPlayerStep(6013) {//草花统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4009(6014) {//草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHAct4010(6015) {//草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    CHClientUp(6016) {//草花活动统计

        @Override
        public boolean chkArgv(Object... argv) {
            return true;
        }
    },
    BattleCallSoldier(6017) {//沙场点兵

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 3;
        }
    },
    ActTreasure(6018) {//活动投资，财神降临

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 2;
        }
    },

    ChBattleMaxLvEvent(6019) {//玩家当前玩法最高关卡出现结果触发

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 4;
        }
    },
    ChInviteReceive(6020) {//玩家当前玩法最高关卡出现结果触发

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    TradeStockChange(6021) {//公司被占

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 1;
        }
    },
    KFMineLose(6022) {//跨服资源矿丢失

        @Override
        public boolean chkArgv(Object... argv) {
            return argv.length == 0;
        }
    },
    GuildWarStart(100001) ,
    GuildWarEnd(100002) ,
    TankUnlockStarNode(100003),// 坦克解锁星级节点
    TankMasterScoreAdd(100004),// 增加坦克图鉴积分
    DriverEvolveStarUp(100005),// 兽魂觉醒
    KFNoMatch(100006),// 跨服轮空

    MainTaskAdd(100007),   //添加主线任务
    MissionOffLineBox(100008),//领取离线宝箱奖励
    ;

    private final int enumId;

    private ObservableEnum(int enumId) {
        this.enumId = enumId;
    }

    public int getEnumId() {
        return this.enumId;
    }

    public static ObservableEnum getObservableEnum(int type) {
        for (ObservableEnum temp : ObservableEnum.values()) {
            if(type == temp.getEnumId()) return temp;
        }
        return null;
    }
    /**
     * 检查发送的参数是否合法
     * @author zigm
     * @param argv
     * @return  使用说明
     *
     */
    public boolean chkArgv(Object... argv) {
        return true;
    }
}
  
