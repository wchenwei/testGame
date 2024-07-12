package com.hm.config;

import cn.hutool.core.date.DateTime;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 游戏静态参数
 * @Author: siyunlong
 * @Version: V1.00
 * @Create Date: 2015-5-8下午3:22:37
 */
public class GameConstants {
	public static final long Server2020Time = new DateTime("20200101", "yyyyMMdd").getTime();
	public static final String AES_KEY = "de7d553fc14f2782";

	public static boolean isOpenDBQueue = false;
	public static final byte TRUE = 1;
	public static final byte FALSE = 0;
	public static final long MILLISECOND = 1l;
	public static final long SECOND = 1000 * MILLISECOND;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;

	public static final int MailExpireDay = 30;//邮件过期时间(邮件删除)


	public static final int LockTroopNum = 3;

	public static final int TroopTankNum = 5;

	public static final int TroopMaxNum = 5;

	//10秒回复一次
	public static int Recover_HP_Interval = 10;
	//和平城
	public static final int PeaceId = 0;
	// 卡昂id
	public static final int Caen = 2;
	//巴黎id
	public static final int ParisId = 5;
	//李昂id
	public static final int LiAng = 8;

	public static final int EquipMaxPos = 8;
	/**
	 * 合服活动持续的天数
	 */
	public static final int MergeServerActivityDays = 5;
	//荣誉证书id
	public static final int HonorCertificateId = 60012;
	//物资储备的白银钥匙对应的id
	public static final int MaterialReserve_Silver_KeyId = 60039;
	//物资储备的黄金钥匙对应的id
	public static final int MaterialReserve_Gold_KeyId = 61025;
	//普通物资找回的比例
	public static final double ResBackRate_Nomal = 0.3;
	public static final int MaxWorldCityId = 74;//世界地图最大城市id

	public static final int RecoveryBattleBuyAddCount = 30;//战场寻宝购买一次增加30次寻宝次数
	public static final int Camp_Official_Level_Not = 1;//阵营等级小于2级时没有等级为1的官员

	public final static List<Integer> WorldBossOpenWeeks = Lists.newArrayList(1, 3, 5);

	public static final int MoonCakeMinSize = 10;//中秋节排行榜，最小上班的月饼数量

	public static final int WorldBoss_EnterLv = 40;//世界boss玩家进入等级限制

	public static final int RecoveryBattle_MustId = 60046;//战场寻宝保底奖励id

	public static final int Commander_PunishLimit = 1;//总统可制裁人数上限

	//柏林战周几开启
	public static final int BerlinWarWeek = 5;
	public static final String BerlinWarWeekStr = "*";
	//跨服远征周几宣战,周1宣战  周2打
	public static final int KfExpeditionWeek = 1;
    public static int Drive_Head_Lv = 10;


	public static final int MinGuildTaskLv = 5;

	public static final int Skip_Guide_Mission = 601;//跳过新手引导到达的关卡

	//世界工程任务开始-结束时间
	public static final int WorldBuildStartHour = 9;
	public static final int WorldBuildEndHour = 22;

	public static boolean FightRecordDB = false;

	public static final int Spring7MaxTimes = 7; //春节7天乐，最大奖励倍数

	public static final int LanternMinSize = 10;//中秋节排行榜，最小上班的月饼数量

	public static final int FrontBattleMaxId = 160004;//前线战争最高品质的敌人

	public static final int FrontBattleMaxQuality = 4;//前线战争最高品质

	public static final int TrainCentreId = 1009;//训练中心的id

	public static final int CommandCentreBuildId = 1001;//指挥中心建筑id
	public static final int BuildExtortMinLv = 6;//解锁征讨的最小建筑登记

	public static final int ArmyRaceNAVY = 1; //海军订单
	public static final int ArmyRaceNLAND = 2; //陆军订单
	public static final int ArmyRaceAIR = 3; //空军订单
	// 五一
	public static final int Act51_Map_Height = 6; //地图高度
	public static final int Act51_Map_Width = 7; //地图宽度
	public static final int Act51_Start_Layer = 5;	// 初始化等级
	public static final int Act51_Brick_Gunpowder_Barrel = 7; //火药桶
	public static final int Act51_Brick_Treasure = 8; //宝藏
	
	//军工厂证书id
	public static final int Guild_Factory_RepairId = 60236; //维修证书id
	//城市获取锁时间
	public static int CityLockSecond = 1;
	//给全体坦克加属性和技能的武器类型
	public static int Arms_All_Type = -1;
	
	public static final int Act55MinSize = 10;//端午节，最小上榜的粽子数量

	public static final int Special_Model_Lv = 1; //特殊勋章等级
	public static final int Special_Model_Star = 2; //特殊勋章星级
	
	public static final int WordFilter_LvLimit = 50; //举报过滤字检查最高等级限制
    public static final int Mastery_Line9 = 9; //研修连线类型9 特殊类型
    public static final int BattleCallSoliderSoulNum = 6; //沙场点兵默认奖励将魂数量
    
	public static final int OnTankDefaultNum = 5;//默认可上阵坦克

	public static final int KfWordWarShopOpen = 75;//在跨服世界大战开启第几天开启商店
	public static final int KfWordWarShopOpenDays = 3;//跨服世界大战商店开启天数

    //玩家名字随机次数上限
    public static final int PLAYER_NAME_RANDOM_MAX = 200;
	public static final int COST_CASH_LIMIT = 300000; //超过 30万的钞票
	public static final int Strength_Store_Limit = 500; // 力量配件 最多 500个
	public static final int Strength_LvCost_Limit = 20; // 升级消耗一次最多数量
	public static final int Login_Welfare_day = 14; // 七日登陆 活动领奖最大次数

	public static final int DEFAULT_ICON_ID = 1; //默认的 icon

	public static final int DayTankTestCount = 10;//每日坦克分析次数
	public static final int DaySSTankTestCount = 3;//ss坦克每日分析次数
	public static int MainBuffLoseTime = 360;//
	public static int Act2063Item = 60537;// 钓鱼大赛积分道具ID

	public static final int FishIntervalHour = 2;// 钓鱼图鉴分享时间间隔
	public static final int Peak_Battle_Star = 25;// 巅峰挑战 超过星级可扫荡

	public static final int CityMaxShowTroop = 5;


	public static boolean checkTroopSize(int size) {
		return size > 0 && size <= 5;
	}


	public static long calDeathHonor(long combat,double rate) {
		return (long)(Math.pow(combat,0.5d)*rate);
	}
}

