package com.hm.enums;

import cn.hutool.core.convert.Convert;
import com.hm.action.battle.Handler.AbstractBattleHandler;
import com.hm.action.mission.biz.PlayerMissionBoxBiz;
import com.hm.action.tank.biz.PaperResearchBiz;
import com.hm.action.tank.biz.ResearchBiz;
import com.hm.action.tank.vo.ItemsVo;
import com.hm.action.vip.VipBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.templaextra.AdTemplate;
import com.hm.config.excel.templaextra.DailyTaskWeekRewardTemplateImpl;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.message.MessageComm;
import com.hm.model.battle.BaseBattle;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.task.Random.BaseRandomTask;

import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 广告类型
 * @date 2023/8/29 13:40
 */
public enum AdsType {
	ArmyFete1(1,"补给1"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			return SpringUtil.getBean(VipBiz.class).getArmyFeteRewards(player);
		}
	},
	ArmyFete2(2,"补给2"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			return SpringUtil.getBean(VipBiz.class).getArmyFeteRewards(player);
		}
	},
	ArmyFete3(3,"补给3"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			return SpringUtil.getBean(VipBiz.class).getArmyFeteRewards(player);
		}
	},

	TankTest(6,"坦克分析"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			int tankId = msg.getInt("tankId");
			Tank tank = player.playerTank().getTank(tankId);
			if (tank == null){
				return null;
			}
			PaperResearchBiz paperResearchBiz = SpringUtil.getBean(PaperResearchBiz.class);
			Items rewards = paperResearchBiz.tankResearch(player, tankId, 5);
			player.sendMsg(MessageComm.S2C_Tank_Research, rewards);
			return null;
		}
	},
	RecoveryBattle(8,"战场寻宝次数"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			player.getPlayerCDs().addCdNum(CDType.BattlefieldTreasure, GameConstants.RecoveryBattleBuyAddCount);
			return null;
		}
		@Override
		public boolean isAfterReward() {
			return true;
		}
	},
	TankResearchSenior(12,"高级研究"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			ResearchBiz researchBiz = SpringUtil.getBean(ResearchBiz.class);
			List<ItemsVo> itemsVos = researchBiz.seniorCustom(player, TankResearchType.Ad);
			player.sendMsg(MessageComm.S2C_TankResearchSenior, itemsVos);
			return null;
		}
	},
	RandomTask(13,"民情任务"){
		@Override
		public List<Items> reward(Player player, AdTemplate template,JsonMsg msg) {
			BaseRandomTask task = player.playerRandomTask().getTask();
			if (task == null) {
				return null;
			}
			task.doFinishAfter(player, true);
			player.playerRandomTask().SetChanged();
			return null;
		}
		@Override
		public boolean isAfterReward() {
			return true;
		}
	},
	DailyTask(15, "周任务高阶") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			// 校验等级限制
			int beginLv = player.playerDailyTask().getBeginLv();
			int weekPoint = player.playerDailyTask().getWeekPoint();
			DailyTaskConfig dailyTaskConfig = SpringUtil.getBean(DailyTaskConfig.class);
			DailyTaskWeekRewardTemplateImpl weekRewardCfg = dailyTaskConfig.getWeekRewardCfg(beginLv, template.getId());
			if (weekRewardCfg == null) {
				return null;
			}
			// 目前用户的活跃等级
			int weekLv = dailyTaskConfig.getWeekLv(weekPoint);
			// 校验活动等级
			if (weekRewardCfg.getWeek_actvie_lv() > weekLv) {
				return null;
			}
			Integer orDefault = player.playerDailyTask().getRewardRec().getOrDefault(weekRewardCfg.getId(), 0);
			// 1: 已领取且只领取普通奖励
			if (orDefault != 1) {
				return null;
			}
			// 高阶奖励记录
			player.playerDailyTask().addRewardRec(weekRewardCfg.getId(), 2);
			return weekRewardCfg.getHighItemsList();
		}
	},
	SevenDay(17, "七日活动") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			return null;
		}
	},

	BeastTrial(41, "兽王试炼") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			player.playerFieldBoss().addFreeCount();
			return null;
		}
	},
	BattleDayReward(42, "战役每日奖励") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			int type = Convert.toInt(template.getParam(), 0);
			BattleType battleType = BattleType.getBattleType(type);
			if (battleType == null){
				return null;
			}
			BaseBattle playerBattle = player.playerBattle().getPlayerBattle(battleType.getType());
			if (playerBattle == null){
				return null;
			}
			AbstractBattleHandler battleHandler = battleType.getBattleHandler();
			return battleHandler.getDayRewards(player, playerBattle);
		}
	},
	ShopRefresh(43, "商店刷新") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			int shopId = Convert.toInt(template.getParam(), 0);
			player.playerShop().getPlayerShop(shopId).refreshOnce(player);
			player.playerShop().SetChanged();
			return null;
		}
	},
	GVESpeed(101, "关卡战斗加速") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			long add = Integer.parseInt(template.getParam());
			player.playerMission().addGveSpeedTime(add);
			return null;
		}
	},
	OnHookBox(102, "挂机宝箱奖励") {
		@Override
		public List<Items> reward(Player player, AdTemplate template, JsonMsg msg) {
			long add = Integer.parseInt(template.getParam());
			return SpringUtil.getBean(PlayerMissionBoxBiz.class).calBoxItemList(player,add);
		}
	},
	;

	private AdsType(int type, String desc) {
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

	public boolean isAfterReward() {
		return false;
	}


	public abstract List<Items> reward(Player player, AdTemplate template,JsonMsg msg);

	public static AdsType getAdsType(int type) {
		for (AdsType buildType : AdsType.values()) {
			if (buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

}