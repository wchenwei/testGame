package com.hm.action.activity;

import com.hm.action.giftpack.GiftPackBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.*;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.RankType;
import com.hm.enums.RechargeType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.PlayerUnlockActivity;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerActivity;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class ActivityObserverBiz implements IObserver{
	@Resource
	private RechargeConfig rechargeConfig;
	@Resource
    private ActivityBiz activityBiz;
	@Resource
	private ActivityConfig activityConfig;

	@Resource
	private GiftPackBiz giftPackBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private ActiveSoldierConfig activeSoldierConfig;
	@Resource
	private Activity69Config activity69Config;
	@Resource
	private ActivitySimpleConfig activitySimpleConfig;
	@Resource
	private ActivityHolidayConfig activityHolidayConfig;
	@Resource
	private Activity202Config activity202Config;
	@Resource
	private ActiveArmyRaceConfig activeArmyRaceConfig;
	@Resource
	private ActivityNavyConfig activityNavyConfig;
	@Resource
	private Activity51Config activity51Config;
	@Resource
	private Active55Config active55Config;
	@Resource
	private ServerMergeConfig serverMergeConfig;
	@Resource
	private ActiveWarmUpGiftConfig activeWarmUpGiftConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ClearnceMission, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CostRes, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddHonor, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginZero, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch(observableEnum) {
			case Recharge :
				int rechargeId = (int)argv[0];
				int giftId = (int)argv[3];
				doRechargeActivity(player, rechargeId, giftId);
				break;
			case MilitaryLvUp:
				checkPlayerUnlockActivity(player,true);
				break;
			case ClearnceMission :
			case PlayerLoginZero:
			case FunctionUnlock:
				checkPlayerUnlockActivity(player,true);
				break;
		}
	}


	private void doMergeServerActivity(Player player, Object[] argv) {
		if (!activityBiz.checkActivityIsOpen(player, ActivityType.MergeServer)) {
			return;
		}
		long count = Long.parseLong(argv[1].toString());
		//刷新排行榜
		HdLeaderboardsService.getInstance().updatePlayerRankForTimeAdd(player, RankType.ActMergeServerGold, count);
    }


	

	/**
	 * doRechargeActivity:(处理充值的活动). <br/>  
	 * @author zxj  
	 * @param player
	 * @param rechargeId  
	 * @param giftid  使用说明
	 */
	private void doRechargeActivity(Player player, int rechargeId, int giftid) {
		RechargePriceNewTemplate rechargeTemplate = rechargeConfig.getTemplate(rechargeId);
		RechargeGiftTempImpl giftTemplate = rechargeConfig.getRechargeGift(giftid);
		if(null==rechargeTemplate || null==giftTemplate) {
			return;
		}
		//检查礼包
		giftPackBiz.addGiftPack(player, giftTemplate);
		//成长计划检查
		doRechargeGrowUp(giftTemplate, player);
		//		doMergeServerRecharge(giftid, player);
	}


	private void doRechargeGrowUp(RechargeGiftTempImpl giftTemplate, Player player) {
		if(giftTemplate.getType() != RechargeType.GrowupRecharge.getType()) {
			return;
		}
		int type = Integer.parseInt(giftTemplate.getExt_info());
		player.playerGiftPack().doBuyFund(type);
	}

	
	/**
	 * 解锁城池处理活动开启
	 * @param player
	 * @param isSendMsg
	 */
	public void checkPlayerUnlockActivity(Player player,boolean isSendMsg) {
		boolean haveUnlockActivity = false;
		PlayerActivity playerActivity = player.getPlayerActivity();
		List<AbstractActivity> activityList = ActivityServerContainer.of(player).getActivityList();
		for (AbstractActivity abstractActivity : activityList) {
			if(abstractActivity instanceof PlayerUnlockActivity) {
				PlayerUnlockActivity playerUnlockActivity = (PlayerUnlockActivity)abstractActivity;
				if((playerUnlockActivity.isCanRepeatUnlock(player) || !playerActivity.isUnlockActivity(abstractActivity.getType()))
						&& playerUnlockActivity.isCanUnlockActivity(player)) {
					playerUnlockActivity.doUnlockActivity(player);
					haveUnlockActivity = true;
				}
			}
		}
		if(isSendMsg && haveUnlockActivity) {
			activityBiz.sendActivityList(player);
		}
	}

}








