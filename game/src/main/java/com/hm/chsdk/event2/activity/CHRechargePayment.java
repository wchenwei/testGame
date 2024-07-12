package com.hm.chsdk.event2.activity;

import com.hm.libcore.spring.SpringUtil;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;


/**
 * 活动金砖消耗
 *
 * @author xb
 */
@EventMsg(obserEnum = ObservableEnum.Recharge)
public class CHRechargePayment extends CommonParamEvent {
    private int pay_id;
    private long vip_amount;
    private long is_first_pay;
    private long pay_amount;


    @Override
    public void init(Player player, Object... argv) {
        int testFlag = (int) argv[2];
        if (testFlag != 1) {//=1是正式充值
            return;
        }
        loadEventType(CHEventType.Activity, 4001, "payment_complete");

        int rechargeId = (int) argv[0];
        int giftId = (int) argv[3];

        RechargeConfig rechargeConfig = SpringUtil.getBean(RechargeConfig.class);
        RechargePriceNewTemplate rechargeTemplate = rechargeConfig.getTemplate(rechargeId);
        this.pay_id = giftId;
        this.is_first_pay = player.getPlayerStatistics().getLifeStatistics(StatisticsType.RECHARGE_COUNT)
                <= 1 ? 1 : 0;
        this.pay_amount = (long) argv[1];
        this.vip_amount = rechargeTemplate.getVip_point();
    }

}
