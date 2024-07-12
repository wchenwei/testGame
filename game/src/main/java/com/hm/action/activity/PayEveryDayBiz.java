package com.hm.action.activity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.PayEveryDayEnum;
import com.hm.model.activity.payeveryday.PayEveryDayValue;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Biz
public class PayEveryDayBiz {
	@Resource
	private RechargeConfig rechargeConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private GiftPackageConfig giftPackageConfig;
    @Resource
    private MailBiz mailBiz;
    @Resource
    private MailConfig mailConfig;

    /**
     * 每日可领取、购买上限
     */
    public static final int MAX_TIMES = 2;

	public void doRecharge(Player player, int rechargeId) {
		PayEveryDayValue actValue = (PayEveryDayValue) player.getPlayerActivity().getPlayerActivityValue(ActivityType.BuyEveryDay);
		//买完直接领取
        List<Integer> payEveryIds = commValueConfig.getPayEveryIds();
        if (payEveryIds.contains(rechargeId)) {
            actValue.buyReward(player, rechargeId);
        } else {
            actValue.addDays(payEveryIds);
		}
		player.getPlayerActivity().SetChanged();
	}

    /**
     * 补领未领取的奖励
     *
     * @param player
     * @param activityValue
     */
    public void sendReward(Player player, PayEveryDayValue activityValue) {
        Date lastLoginDate = player.playerBaseInfo().getLastLoginDate();
        DateTime date = DateUtil.date();
        int between = (int) DateUtil.betweenDay(lastLoginDate, date, true);

        List<Integer> payEveryIds = commValueConfig.getPayEveryIds();
        Map<Integer, Integer> buyState = activityValue.getBuyState();
        int dayPoints = 0;
        List<Items> items = Lists.newArrayList();
        // 是否还有旧数据未处理
        boolean isOldOver = activityValue.getDayMap().values().stream().noneMatch(n -> n > 0);
        if (isOldOver) {
            for (Integer id : payEveryIds) {
                // 玩家已购总次数
                int max = activityValue.getRewardMap().getOrDefault(id, 0);
                int n = Math.min(max, between);
                // 扣除当天领取
                if (activityValue.getTodayReward().contains(id)) {
                    n--;
                }
                if (n <= 0) {
                    continue;
                }

                RechargeGiftTempImpl rechargeGift = rechargeConfig.getRechargeGift(id);
                List<Items> giftList = giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift());
                items.addAll(ItemUtils.calItemRateReward(giftList, MAX_TIMES * n));
                dayPoints += commValueConfig.getPayEveryPoint(id) * (MAX_TIMES * n);
                activityValue.getRewardMap().merge(id, -n, Integer::sum);
            }
        }

        for (Integer id : payEveryIds) {
            // 可领取天数
            int days = activityValue.getDays(id);
            int min = Math.min(days, between);
            RechargeGiftTempImpl rechargeGift = rechargeConfig.getRechargeGift(id);
            List<Items> giftList = giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift());
            if (buyState.getOrDefault(id, 0) == PayEveryDayEnum.Get.getState()) {
                min -= 1;
            }
            if (min <= 0) {
                continue;
            }
            items.addAll(ItemUtils.calItemRateReward(giftList, min));
            dayPoints += commValueConfig.getPayEveryPoint(id) * (min);
            activityValue.operationDay(id, min);
        }

        // 添加 VIP 点数
        activityValue.incPoint(dayPoints);
        MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.PayEveryDay);
        List<Items> backReward = ItemUtils.mergeItemList(items);
        if (CollUtil.isNotEmpty(backReward)) {
            mailBiz.sendSysMail(player, mailTemplate, backReward);
        }
    }
}
