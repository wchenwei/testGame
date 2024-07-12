package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.leaderboards.RankRedisUtils;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.kf.kfworldwar.KfWorldWarGameBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.kfseason.server.KFSeasonUtil;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ItemUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @Description: 排行榜类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum RankType {
	Combat(1,"部队战力榜","combat",false,false,false) ,
	CityKillTank(4,"城战杀敌数","CityKillTank",true,true,false),
	Arena(5,"竞技场","arena",false,true,false),
	CityOccupy(8,"攻占城池","CityOccupy",true,true,false),
	TowerRank(9,"使命之路爬塔排行榜","towerRank",false,false,false),
	TotalHonor(10,"累计荣誉排行","TotalHonor",false,false,false),
	PlayerMainBattle(14,"玩家主关卡排行","PlayerMainBattle",false,false,false),
	Shooting(15,"坦克打靶排行","shooting",true,true,false),
	SevenPlayerHonorRank(18,"七日争霸玩家排行","SevenPlayerHonorRank",false,false,false),
	SevenGuildHonorRank(19,"七日争霸部落排行","SevenGuildHonorRank",false,false,false),
	OverallWarRank(200,"全面战争排行榜","overallWarRank",true,true,false),
	AnswerQuestionRank(201,"答题排行榜","answerNew",true,true,false),
	KfSportsRank(27, "跨服竞技战排行", "kfSportsRank", false, false,false),
	Recharge(28, "充值金额排行榜", "rechargeRank", false, false,false),

	GhostKillRank(30, "幽灵杀敌排行榜或国战期间杀敌排行", "GhostKill", false, false,false),
	KfManorScore(31,"跨服领地战玩家贡献排行","KfManorScore",false,false,false),
	ResSuddenStrike(32,"物资突袭排行榜","ResSuddenStrike",false,false,false),
	MaterialReserve(33,"物资集结活动排行","MaterialReserve",false,false,false),
	ArmyDayBossAtk(34,"81活动boss伤害排行","ArmyDayBoss",true,false,false),
	TroopCombat(35,"玩家部队战力排行","TroopCombat",false,false,false),
	ResScoreRank(36,"资源跨服积分排行","ResScore",false,false,false),
	WorldBoss(42,"叛军要塞","worldBoss",false,false,false),
	PlayerGuildScore(43,"玩家部落积分","PlayerGuildScore",true,true,false),
	KfServerScore(44,"跨服积分战排行","KfServerScore",false,false,false),//只用于发奖励
	DreamlandBattle(53,"梦回沙场军工排行","DreamlandBattle",false,false,false),
	BuildCombat(54,"解锁建筑征讨的战力排行","BuildCombat",false,false,false),
	RepairTrain(67,"维修训练","RepairTrain",true,true,false),
	MineRank0(73,"跨服资源战0","MineRankLow",false,false,false),
	MineRank1(74,"跨服资源战1","MineRankMid",false,false,false),
	MineRank2(75,"跨服资源战2","MineRankHight",false,false,false),
	MineRank3(76,"跨服资源战3","MineRankCenter",false,false,false),

	ActMergeServerGold(89,"合服狂欢期间.金砖消耗榜","mergeServerGoldUsed",false,false,false),
	SeasionPlayerScore(103,"赛季玩家积分","SeasionPlayerScore",false,false,false){
		@Override
		public String getRankName() {
			return rankName+KFSeasonUtil.getCurSeason().getId();
		}
	},
	ArmyPressBorder(106,"大军压境","ArmyPressBorder",false,false,false),
	KFWorldWarDonate(107, "世界大战贡献榜", "WorldWarDonate", false, false,false) {
		@Override
		public String getRankName() {
			return KfWorldWarGameBiz.buildKFWorldRankName(new Date());
		}
	},
	RechargeNewActivity(174, "充值榜", "RechargeNewActivity", false, false,false),
	KfExpeditionExploits(214,"远征战功排行","KfExpeditionExploits",false,false,false),

	SmallGameRank(501,"小游戏排行榜奖励","SmallGameRank",true,true,false),
	GuildHonor(502,"阵营荣誉排行","CampHonor",false,false,false) {
		@Override
		public String getRankName(BasePlayer player) {
			return getRankName()+player.getGuildId();
		}
	},
	TankResearch(503, "高研排行", "TankResearch", false, false,false),
	TradeCombatServer(504, "贸易股东战力[本服]", "TradeCombatServer", false, false, false),
	TradeCombatKF(505, "贸易股东战力[跨服]", "TradeCombatKF", false, false, false),
	FieldBoss(601, "兽王试炼", "FieldBoss", false, true, true){
		@Override
		public String getRankName() {
			return getRankName(new Date());
		}
		@Override
		public String getRankName(String mark) {
			return rankName +":"+ mark;
		}
		@Override
		public String getRankName(Date date) {
			return rankName + ":" + RankRedisUtils.createDateMark(date);
		}
	},

//排行榜最大1000000；超过这个对gm查询有影响（用户管理->玩家管理->排序方式中--角色等级，vip等级查询有影响）
	;


	private RankType(int type, String desc,String rankName,boolean dayReset,boolean dayReward, boolean dayResetName) {
		this.type = type;
		this.desc = desc;
		this.rankName = rankName;
		this.dayReset = dayReset;
		this.dayReward = dayReward;
		this.dayResetName = dayResetName;
	}
	
	public static RankType getTypeByIndex(int type) {
		for (RankType buildType : RankType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	
	//获取每日重置排行榜
	public static List<String> getDayResetRanks() {
		return Arrays.stream(RankType.values()).filter(e -> e.isDayReset())
				.map(e -> e.getRankName())
				.collect(Collectors.toList());
	}

	
	public static List<RankType> getDayRewardRanks() {
		return Arrays.stream(RankType.values()).filter(e -> e.isDayReward())
				.collect(Collectors.toList());
	}

	private int type;
	private String desc;
	protected String rankName;
	private boolean dayReset;//是否每日重置
	private boolean dayReward;//是否每日发放奖励
	private boolean dayResetName;// 是否重置名称


	public String getRankName(BasePlayer player) {
		return getRankName();
	};

	public String getRankName(String mark){
		return getRankName();
	}

	public String getRankName(Date date){
		return getRankName();
	}

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	public String getRankName() {
		return rankName;
	}
	public String getConfRankName() {
		return rankName;
	}

	public boolean isDayReset() {
		return dayReset;
	}

	public boolean isDayReward() {
		return dayReward;
	}

	public boolean isDayResetName() {
		return dayResetName;
	}

	//是否需要将分数转化为int
	public static boolean isScoreToInt(RankType type){
		return type!=RankType.Shooting&&type!=RankType.AnswerQuestionRank&&type!=RankType.ResSuddenStrike&&type!=RankType.OverallWarRank;
	}
	
	public static ActivityType getActivityTypeByRankType(RankType rankType){
		return getActivityTypeByRankType(rankType.getType());
	}
	public static ActivityType getActivityTypeByRankType(int rankType){
		return null;
	}

	//根据排行榜类型，积分和排行基础奖励获取最终奖励
	public static List<Items> getRankRewardsByScore(RankType type,List<Items> baseRewards,double score){
		List<Items> rewards = Lists.newArrayList(baseRewards);
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		try {
			switch(type) {
//				case RechargeCarnival:
//				case RechargeCarnival2:
//					double scoreLimit = commValueConfig.getCommValue(CommonValueType.RechargeCarnivalDoubleLimit);
//					if(score>=scoreLimit) {
//						rewards = ItemUtils.calItemExtraAdd(rewards,1);
//					}
			}
		} catch (Exception e) {
		}
		return rewards;
	}

	//获取活动排行榜
	public String getActivityRankName(BasePlayer player, ActivityType activityType, RankType rankType){
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if (activity != null){
			return activity.getActivityRankName(rankType);
		}
		return null;
	}

	private static boolean isBetween(int value, int start, int end) {
		return value>=start && value<=end;
	}
}
