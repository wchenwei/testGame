/**  
 * Project Name:SLG_GameHot.
 * File Name:MailConfigEnum.java  
 * Package Name:com.hm.enums  
 * Date:2018年4月10日下午3:20:04  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.enums;

/**  
 * ClassName: MailConfigEnum. <br/>  
 * Function: 邮件内容配置信息的枚举. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月10日 下午3:20:04 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public enum MailConfigEnum {
	//此枚举的type，对应mail.xlsx配置文件中的id
	GuildRefuse(3,"部落申请被拒绝"),
	GuildAgree(4,"同意部落申请"),
	GuildCityQuit(5,"退出部落时发送奖励邮件"),
	ArenaRankReward(6,"竞技场排行奖励"),
	KillRankReward(7,"杀敌排行奖励"),
	OccCityRankReward(8,"占城排行奖励"),
	Pvp1v1RankReward(9,"偷袭排行奖励"),
	YuekaReward(10,"月卡奖励"),
	ZhoukaReward(11,"周卡奖励"),
	HonorReward(12,"未领取荣誉奖励邮件"),
	FinanceTatics(13,"金融战术"),
	Shooting(14,"坦克打靶"),
	ThreeDayReward(15,"3日活动"),
	RechargeRebate(16,"内测充值返利"),
	Combat(17,"冲榜战力榜奖励"),
	PlayerExp(18,"冲榜等级榜奖励"),
	PlayerMainBattle(19,"冲榜通关榜奖励"),
	SevenDayReward(20,"7日充值活动奖励"),
	RichManRankReward(22,"清明活动排行奖励"),
	CloneTroopReturn(23,"镜像返还通知"),
	RichManRankDayLogin(24,"清明活动每日登陆奖励"),
	RichManRankReward2(25,"五一活动排行奖励"),
	SevenPlayerHonor(27,"玩家七日争霸活动"),
	SevenGuildHonor(28,"部落七日争霸活动"),
	GuildImpeachSucc(29,"部落弹劾成功"),
	GuildImpeachFail(30,"部落弹劾失败"),
	AnswerQuestionRankReward(31,"答题排行奖励"),
	TrumpWinReward(32,"未领取王牌军演奖励"),
	TrumpRankReward1(33,"王牌军演青铜奖励"),
	TrumpRankReward2(34,"王牌军演白银奖励"),
	TrumpRankReward3(35,"王牌军演黄金奖励"),
	TrumpRankReward4(36,"王牌军演白金奖励"),
	TrumpRankReward5(37,"王牌军演钻石奖励"),
	KfBetWin(38,"跨服战押注成功"),
	KfBetLose(39,"跨服战押注失败"),
	KfSportsRank(40,"跨服战排行"),
	CampApplyResult(41,"阵营官职投票结果"),
	CampImpeachFail(42,"阵营弹劾失败"),
	CampImpeachSuc(43,"阵营弹劾成功"),
	MergeServerHonor(44,"合服荣誉榜排行奖励"),
	MergeServerTankCombat(45,"合服战车榜排行奖励"),
	MergeServerCombat(46,"合服战力榜排行奖励"),
	MergeServerRecharge(47,"合服充值榜排行奖励"),
	MergeServerReward(48,"合服补偿奖励"),
	MergeServerRechargeReward(49,"合服充值惊喜奖励"),
	PveRebelOccupy(50,"首日PVE攻占奖励"),
	PveRebelOver(51,"清除残党活动结束通知"),
	RichManRankReward3(52,"端午活动排行奖励"),
	GhostKill(53,"幽灵杀敌排行"),
	GuildBarackWarReward(54,"国战中部落调兵的奖励"),
	GuildBerlinWin(55,"首都战银条奖励"),
	GhostBossReward(56,"幽靈BOSS奖励"),
	BerlinWinReward(57,"大总统奖励"),
	Fetters(58,"坦克羁绊重置补偿"),
	KfManorReward(59,"跨服领地战奖励"),
	ResSuddenStrikeReward(60,"物资突袭排行奖励"),
	BerlinTargetReward(61,"首都战目标奖励"),
	ConsumeGoldReward(62,"开采金矿活动奖励"),
	MaterialReserveReward(63,"物资储备排行奖励"),
	FundGroupon(64,"基金团购奖励"),
	/*ArmyDayBossKill(65,"81活动--boss击杀奖励"),
	ArmyDayBossRandom(66,"81活动--boss概率宝箱奖励"),
	ArmyDayBossPoint(67,"81活动--boss关键伤害奖励"),*/
	ArmyDayBossRank(68,"81活动--boss伤害排行奖励"),
	OverallWarRank(69,"全面战争奖励排行"),
	KFResScore(70,"跨服资源战积分排行奖励"),
	ArmyDayBossReward(71,"81活动--奖励邮件"),
	ValentineDayLogin(72,"七夕活动每日登陆奖励"),
	CampWage(73,"阵营官员工资"),
	MoonCakeAll(74,"总排行"),
	MoonCakeWR(75,"五仁"),
	MoonCakeDS(76,"豆沙"),
	MoonCakeLR(77,"莲蓉"),
	MoonCakeDH(78,"莲蓉蛋黄"),
	WorldBossKill(79,"世界boss击杀奖励"),
	WorldBossRankReward(80,"世界boss排行奖励"),
	KfExpeditionOil(82,"【跨服邮件】跨服远征反返石油"),
	GuildWar5MainCity(83,"黑鹰国战主城奖励"),
	
	MergeRewardTitle(84, "【合服邮件】合服称号取消补偿"),
	MergeRewardServer(85, "【合服邮件】合服补偿"),
	KfManorResBack(86, "【跨服邮件】跨服领地石油返还"),
	ActivityReward59(87,"连续充值活动奖励"),
	GuildScoreReward(88,"部落任务积分个人排行奖励"),
	DailyTaskReward(89,"未领取周活跃奖励"),
	KfScoreResBack(90, "【跨服邮件】跨服积分战石油返还"),
	KfPkLevelReward(91, "【跨服邮件】跨服段位赛段位奖励"),
	
	LanternAll(93, "元宵节排行-总榜"),
	Lantern1(94, "元宵节排行-五仁"),
	Lantern2(95, "元宵节排行-豆沙"),
	Lantern3(96, "元宵节排行-莲蓉"),
	Lantern4(97, "元宵节排行-蛋黄"),
	ActivityReward74(98,"新春奖池瓜分奖励"),
	ValentineDayLogin2(99,"情人节活动每日登陆奖励"),
	ChSwShopReward(100,"草花神秘商店线下"),
	LvCompensate(101,"玩家等级补偿"),
	ValentineDayLogin314(102,"白色情人节活动每日登陆奖励"),
	AnnsiversaryRankReward(103,"周年活动排行奖励"),

	ArmyRaceAll(104,"军备竞赛军力排行"),
	ArmyRaceNavy(105,"海军力排行"),
	ArmyRaceLand(106,"陆军力排行"),
	ArmyRaceAir(107,"空军力排行"),
	OneDollarCancel(108, "1元夺宝-奖券返还"),
	OneDollarPrize(109, "1元夺宝-奖励发放"),
	NavyAll(110,"海军节排行-总榜"),
	NavyAirplane(111,"海军节排行-飞机榜"),
	NavyBattleship(112,"海军节排行-战舰榜"),
	NavySubmarine(113,"海军节排行-潜艇榜"),
	Activity51(114,"劳动节排行"),
	RepairTrainRankReward(115,"维修训练排行奖励"),
	WzBackOilReward(116,"王者峡谷资源返还"),
	Act55All(117,"粽子总排行"),
	Act55_1(118,"红豆棕"),
	Act55_2(119,"蜜枣粽"),
	Act55_3(120,"莲子粽"),
	Act55_4(121,"莲子鲜肉棕"),
	ValentineDayLogin520(122,"520情人节活动每日登陆奖励"),
	Act55All2(123,"粽子总排行"),
	Act55_12(124,"红豆棕"),
	Act55_22(125,"蜜枣粽"),
	Act55_32(126,"莲子粽"),
	Act55_42(127,"莲子鲜肉棕"),
	MergeRewardKingTitle(128,"王者称号取消奖励"),
	BerlinBossOpen(129,"最后的战意开启奖励"),
	RechargeCarnivalRankMail(130,"充值狂欢排行奖励"),
	ConsumeCarnivalRankMail(131,"消费狂欢排行奖励"),
	Act97RechargeRank(132,"6周年充值排行榜"),

	MergeServerGold(133,"合服消费榜排行奖励"),
	Act97RechargeRank2(134,"6周年充值排行榜第二期，感恩节充值排行奖励"),

	Act55All3(135,"粽子总排行"),
	Act55_13(136,"红豆棕"),
	Act55_23(137,"蜜枣粽"),
	Act55_33(138,"莲子粽"),
	Act55_43(139,"莲子鲜肉棕"),
	KFScuffleBackOilReward(140,"跨服乱斗资源返还"),
	ActWinterFish(141,"冬捕节排行(五一活动二期)"),
	KFSeasonServerReward(142,"跨服赛季-服务器奖励"),
	KFSeasonScoreReward(143,"跨服赛季-个人积分奖励"),
	KFSeasonScoreTopTitle(144,"跨服赛季-第一名称号"),
	CarrierStrike(145,"航母出击排行 邮件"),
	CarrierStrikeTwo(147,"航母出击(第二期) 邮件"),
	LevelEventReturn(146,"等级事件改版奖励返还"),
    LevelEventReturnCheck(148, "等级事件改版奖励返还"),
    ArmyPressRank(149, "大军压境排行奖励"),
	WorldWarImpeachSucc(150, "世界大战弹劾成功"),
	WorldWarImpeachFail(151, "世界大战弹劾失败"),
	WorldWarFirstOcc(152, "世界大战首胜奖励"),
	WorldWarWeekReward(153, "世界大战每周贡献排行奖励"),
	WorldWarEndReward(154, "世界大战结束(石油返还邮件)"),
	WorldWarEndRewardAndTitle(155, "世界大战结束(石油返还+称号邮件)"),
	WorldWarTeamTaskComplete(156, "世界大战团队任务完成"),
	BackFlow(157, "老用户回流奖励"),
	MoonCake2021All(158,"总排行"),
	MoonCake2021WR(159,"五仁"),
	MoonCake2021DS(160,"豆沙"),
	MoonCake2021LR(161,"莲蓉"),
	MoonCake2021DH(162,"莲蓉蛋黄"),
	MoonCake2021Red(163,"中秋锦鲤大奖"),
	Act97RechargeRank3(164, "6周年充值排行榜第三期，感恩节充值排行奖励"),
	Act97RechargeRank4(165, "6周年充值排行榜第四期，感恩节充值排行奖励"),
	Act97RechargeRank5(166, "6周年充值排行榜第五期，感恩节充值排行奖励"),
	Act113Reward(167, "活动奖励未领取"),
	Act113Rank1(168, "新增亲密榜"),
	Act113Rank2(169, "新增充值榜"),
	Act113Rank3(170, "新增消费榜金砖"),
	SmallGameRank(171, "小游戏邮件"),
	Act97RechargeRank7(172, "春日狂欢充值排行奖励,97活动第7期"),

    Act20Rank1(173, "大富翁1期排行-次数榜"),
    Act20Rank2(174, "大富翁1期排行-消费榜"),
    Act20Rank3(175, "大富翁2期排行-次数榜"),
    Act20Rank4(176, "大富翁2期排行-消费榜"),
    Act20Rank5(177, "大富翁3期排行-次数榜"),
    Act20Rank6(178, "大富翁3期排行-消费榜"),
    Act20Rank7(179, "大富翁4期排行-次数榜"),
    Act20Rank8(180, "大富翁4期排行-消费榜"),

	Act97RechargeRank8(181, "97活动第8期"),
	Act97RechargeRank9(182, "97活动第9期"),

	Recharge120RankMail(183,"120活动充值狂欢排行奖励"),
	Act121RechargeRank(184, "国庆节充值排行奖励"),
	Act121RechargeStage2Rank(185, "冬日狂欢充值排行奖励"),
	Act117LastReward(186, "【14日活动】灰熊升星礼包ID，12月6日补偿,12月7日登陆邮件自动发送"),
    TreeReward(187,"植树未领取奖励"),
    TreeReward2(188,"植树驻防奖励"),
	CampHonor(190,"阵营荣誉排行榜"),
	TankResearch(191,"冲榜高研榜奖励"),
	KfExpeditionReward(195,"跨服远征未领取奖励"),
	TradeOwnerChange(196,"大股东变更"),
	TradeRes(197,"大股东每日资源"),
	AgentDispatch(199,"特工派遣奖励"),
	TradeSelfOwnerChange(200,"大股东变更"),
	WeekArenaReward(204,"角斗场周排行奖励"),

	Act55All5(301,"粽子总排行"),
	Act55_15(302,"红豆棕"),
	Act55_25(303,"蜜枣粽"),
	Act55_35(304,"莲子粽"),
	Act55_45(305,"莲子鲜肉棕"),

	FieldBoss(401,"兽王试炼排行奖励"),
	SevenDayLogin(402,"7日登录"),
	FirstJoinGuild(403,"首次加入部落奖励"),


	ZeroGift1(2001, "0元上校礼包"),
	ZeroGift2(2002, "0元上将礼包"),
	ZeroGift3(2003, "0元元帅礼包"),
    PayEveryDay(2050, "每日必买"),

	Act55All4(2051,"粽子总排行"),
	Act55_14(2052,"红豆棕"),
	Act55_24(2053,"蜜枣粽"),
	Act55_34(2054,"莲子粽"),
	Act55_44(2055,"莲子鲜肉棕"),
    TankLotteryReward(2056, "空中拦截大奖邮件"),
	Act_55_Three(2057, "五一活动三期"),

	Act_2063(2059, "钓鱼大赛排行"),
	WXGiftMail(3000, "微信每日礼物邮件"),
	Act2058(3001, "转端充值返利"),
	ArenaRankWeekReward(3002,"竞技场周排行奖励"),
	KFNoMatch(3003,"跨服轮空"),
	KFScoreRankReward(3006,"跨服积分排行奖励"),


    //-----------暂时没有
	NineYouGameGift(6001, "九游礼包"),
	ActivityReward(99999,"活动奖励"),
	;
	
	private MailConfigEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	private String desc;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	/**
	 * 此邮件类型不在对石油预警
	 * @param mailId
	 * @return
	 */
	public static boolean isNoFiterOilMail(int mailId) {
		return mailId == MailConfigEnum.KfExpeditionOil.getType() || 
				mailId == MailConfigEnum.KfScoreResBack.getType()||
				mailId == MailConfigEnum.KfManorResBack.getType();
	}
	
	public static MailConfigEnum getMailType(int type){
		for (MailConfigEnum kind : MailConfigEnum.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
	public static MailConfigEnum getRankRewardMail(RankType rankType) {
		switch (rankType) {
			case Arena:
				return MailConfigEnum.ArenaRankReward;
			case CityKillTank:
				return MailConfigEnum.KillRankReward;
			case Shooting:
				return MailConfigEnum.Shooting;
			case SevenPlayerHonorRank:
				return MailConfigEnum.SevenPlayerHonor; 
			case SevenGuildHonorRank:
				return MailConfigEnum.SevenGuildHonor; 
			case AnswerQuestionRank:
				return MailConfigEnum.AnswerQuestionRankReward;
			case KfSportsRank:
				return MailConfigEnum.KfSportsRank;
			case GhostKillRank:
				return MailConfigEnum.GhostKill;
			case ResSuddenStrike:
				return MailConfigEnum.ResSuddenStrikeReward;
			case MaterialReserve:
			    return MailConfigEnum.MaterialReserveReward;
			case ArmyDayBossAtk:
			    return MailConfigEnum.ArmyDayBossRank;
			case ResScoreRank:
			case MineRank0:
			case MineRank1:
			case MineRank2:
			case MineRank3:
			    return MailConfigEnum.KFResScore;
			case OverallWarRank:
			    return MailConfigEnum.OverallWarRank;
			case WorldBoss:
			    return MailConfigEnum.WorldBossRankReward;
			case PlayerGuildScore:
			    return MailConfigEnum.GuildScoreReward;
			case RepairTrain:
				return MailConfigEnum.RepairTrainRankReward;
            case ArmyPressBorder:
                return MailConfigEnum.ArmyPressRank;
			case KFWorldWarDonate:
				return MailConfigEnum.WorldWarWeekReward;
			case RechargeNewActivity:
				return MailConfigEnum.Recharge120RankMail;
			case GuildHonor:
				return MailConfigEnum.CampHonor;
			case FieldBoss:
				return MailConfigEnum.FieldBoss;
		}
		return null;
	}
	
}
