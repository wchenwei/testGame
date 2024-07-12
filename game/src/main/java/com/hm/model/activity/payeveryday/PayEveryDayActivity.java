package com.hm.model.activity.payeveryday;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.activity.PayEveryDayBiz;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.ActivityType;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * ClassName: PayEveryDayActivity. <br/>
 * Reason: 每日必买活动. <br/>
 * date: 2019年6月12日 下午5:41:03 <br/>
 * @author zxj
 * @version
 */

public class PayEveryDayActivity extends AbstractActivity{

	public PayEveryDayActivity() {
		super(ActivityType.BuyEveryDay);
	}

	@Override
	public List<Items> getRewardItems(BasePlayer player, int id) {
		RechargeConfig rechargeConfig = SpringUtil.getBean(RechargeConfig.class);
		GiftPackageConfig giftPackageConfig = SpringUtil.getBean(GiftPackageConfig.class);
		RechargeGiftTempImpl template = rechargeConfig.getRechargeGift(id);
		if(null==template) {
			return null;
		}
		PayEveryDayValue activityValue = (PayEveryDayValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.BuyEveryDay);
		List<Items> items = giftPackageConfig.rewardGiftList(template.getSpecail_gift());
		if (!activityValue.isOldVersion(id)) {
			return ItemUtils.calItemRateReward(items, PayEveryDayBiz.MAX_TIMES);
		}
		return items;
	}

	@Override
	public void checkPlayerLogin(Player player) {
		PayEveryDayValue activityValue = (PayEveryDayValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.BuyEveryDay);
		if (!activityValue.check()) {
			if (activityValue.dayReset()) {
				player.getPlayerActivity().SetChanged();
			}
			return;
		}
		PayEveryDayBiz payEveryDayBiz = SpringUtil.getBean(PayEveryDayBiz.class);
		payEveryDayBiz.sendReward(player, activityValue);
		activityValue.dayReset();
		player.getPlayerActivity().SetChanged();
	}


	@Override
	public void checkPlayerActivityValue(Player player) {
		player.getPlayerActivity().getPlayerActivityValue(ActivityType.BuyEveryDay);
	}


}





