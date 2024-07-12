package com.hm.enums;

/**
 * @Description: 统计类型
 * @author siyunlong  
 * @date 2018年3月21日 上午10:56:42 
 * @version V1.0
 */
public enum StatisticsType {
    MLv(1,"昨日军衔等级"),

    LOGIN_DAYS(2,"累计登录天数"),
	RECHARGE(3,"累计充值数(人民币)，带测试数据"),
	RECHARGE_COUNT(4,"累计充值数次数"),
	ONLINE_TIME(5,"在线时长(秒)"),
	Diamond(6,"累计充值钻石"),
	RECHARGEReal(7,"累计充值数(人民币)，实际充值。"),
	ReNameCount(8,"改名次数"),
	GuildMoveMainCity(9, "部落主城迁移"),
	GuildWarLogin(10, "国战期间登录"),
	GoldUsed(11, "金币总消耗,gold + sysGold"),
    OilUsed(12, "石油总消耗"),
    DailyTaskComplete(13, "累计完成的日常任务数"),
    ArenaWinTimes(14,"竞技场累计胜利次数"),
    CityWarWinTimes(15,"累计城战胜利次数"),
    ArmsBuyTimes(16,"军火商累计购买次数"),
    DepotTimes(17,"累计军械库运输次数"),
    TowerTimes(18,"参加爬塔玩法次数"),
    SupplyTroop(19,"补给部队参与次数"),
    SupplyRobTroop(20,"成功抢劫补给部队次数"),
    CityWarTotalTimes(21,"累计城战次数"),
    CityWarKillTankNum(22,"累计杀敌数量"),
    SpendGold(23,"累计消耗金砖数量"),
    KingWorship(24,"总统膜拜"),
    KfSportsWorship(25,"跨服竞技总统膜拜"),
    CloneTroopItemCount(27,"克隆宝箱获取数量"),
    CloneTroopCreate(28,"今日克隆部队数量"),
    MedalDayReward(29,"勋章每日奖励"),
    IosUpdateReward(30,"ios小包奖励"),
    FbId(31,"副本id"),

    // ======================================================
    TankSeniorResearchDiscount(43, "tank高级研发半价次数"),
    TankFactoryTimes(44, "客栈绘制次数"),
	TankSeniorResearch(45, "tank高级研发次数"),
	Contr(46,"获得功勋"),
	
	GuildCash(51, "部落捐赠，钞票"),
	GuildGold(52, "部落捐赠，金币"),
	RECHARGEDAY(53,"当日累计充值数(人民币)，带测试数据，是表中配置的人民币数"),
	VipExp(55,"VIP点数"),
	
	RECHARGE_NOACCOUNT(56,"不计首冲的充值金额"),
	AgentBuyTimes(57, "特工购买体力次数"),
    Activity101Pray(58, "国庆祈福次数(天)"),
    RepairTrainCount(59,"维修训练次数(每天)"),
    ArmsStrengthCount(60,"武器强化次数(每天)"),
    RepairTrainFinishCount(61,"维修训练完整完成次数(每天)"),
    
    TroopAdvance(62,"部队突进"),
    TroopRetreat(63,"部队撤退"),
    PvpOneByOneLaunch(64,"偷袭发起"),
    
    Activity81Pray(65, "81祈福次数(天)"),
    GrowUpFundNum(67,"成长基金出现的次数"),
	FinanceTaticsReward(68, "金融战领取次数"),


    Blue_Vip(69, "蓝钻礼包，等级要求是蓝钻等级，每日领1次"),
    blue_year_vip(70, "年费蓝钻礼包，只有年费蓝钻用户，每日领1次"),
    super_blue_vip(71, "豪华蓝钻礼包，只有豪华蓝钻用户，每日领1次"),
    Strength_Buy(73, "力量研发许可每日可用金砖购买"),

    GuildChangeFlag(101,"部落修改旗帜"),

    ADD_DESKTOP_REWARD (1001,"添加桌面赠礼"),
    GZYXQ (1002,"关注游戏圈奖励领取"),
    DY_Sidebar(1003, "抖音侧边栏"),
	;
	
	
	private StatisticsType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

    public static StatisticsType getByType(int type){
        for(StatisticsType statisticsType : StatisticsType.values()){
            if(statisticsType.getType() == type){
                return statisticsType;
            }
        }
        return null;
    }

	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	
}
