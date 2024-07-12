package com.hm.action.treasury.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.treasury.vo.CollectVo;
import com.hm.action.vip.VipBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.LevyConfig;
import com.hm.config.MissionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.PlayerRewardMode;
import com.hm.enums.VipPowType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Biz
public class TreasuryBiz {
	@Resource
	private PlayerLevelConfig playerLevelConfig;
	@Resource
	private LevyConfig levyConfig;
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private VipBiz vipBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	@Resource
	private CommValueConfig commValueConfig;

	public int getBuySpend(int type,int count) {
		return levyConfig.getLevyCost(type,count+1);
	}
	
	public CollectVo createTreasuryForItem(Player player,int count,int type) {
		int critical = levyConfig.getCritRate(count, type);
		//奖励
		Items reward = playerLevelConfig.getLevy(player,type);
		reward.setCount(reward.getCount()*critical);
		//计算双倍活动数量
//		reward = activityEffectBiz.calActivityEffectItemAdd(player, reward, PlayerRewardMode.Treasury);
		return new CollectVo(reward, critical);
	}


	/**
	 * 快速征收需要的金砖数
	 * < 0 时无法征收，可能为0，为0免费
	 *
	 * @param player
	 * @return
	 */
	public int calcCollectPrice(Player player) {
		// 已征收次数
		int cnt = player.playerTreasury().getCostTimes();
		int limit = vipBiz.getVipPow(player, VipPowType.CostCollectionBuy);
		if (cnt >= limit) {
			return -1;
		}
		return missionConfig.getBuyChanceTemplate(cnt+1).getNew_levy();
	}

	/**
	 * 快速征收奖励内容
	 *
	 * @param player
	 * @return
	 */
	public List<Items> cacCostReward(Player player) {
		int interval = commValueConfig.getCommValue(CommonValueType.TreasuryCostInterval);
		return cacReward(player, interval);
	}

	/**
	 * 征收奖励
	 *
	 * @param player
	 * @param minutes
	 * @return
	 */
	public List<Items> cacReward(Player player, int minutes) {
		int cityId = player.playerMission().getRelOpenCity();
		CityTemplate cfg = cityConfig.getCityById(cityId);
		if (cfg == null) {
			return Lists.newArrayList();
		}

		// 普通产出 单位是 小时！！！
		List<Items> base = cfg.getLevyResPerHour();

		List<Items> items = ItemUtils.calItemRateReward(base, MathUtils.div(minutes, TimeUnit.HOURS.toMinutes(1)));
		// 稀有掉落，可能为空 单位是分钟
		List<Items> levyItems = cfg.getLevyItems(minutes);

		items.addAll(levyItems);

		//计算双倍活动数量
		return items;
	}

	public Long calcFullTime(Player player) {
		long lastTime = player.playerTreasury().getLastTime();
		int maxInterval = commValueConfig.getCommValue(CommonValueType.TreasuryMaxInterval);
		//订阅vip
		if(player.getPlayerRecharge().haveSubscribeVip()) {
			maxInterval = commValueConfig.getCommValue(CommonValueType.SubVipTreasuryMax);
		}

		return lastTime + maxInterval * GameConstants.MINUTE;
	}

	public boolean isFull(Player player) {
		return calcFullTime(player) >= System.currentTimeMillis();
	}
}
