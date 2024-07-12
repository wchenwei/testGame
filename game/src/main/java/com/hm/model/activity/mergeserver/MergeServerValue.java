package com.hm.model.activity.mergeserver;

import com.google.common.collect.Maps;
import com.hm.config.GameConstants;
import com.hm.model.activity.PlayerActivityIdListValue;

import java.util.Arrays;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-26
 *
 * @author Administrator
 */
public class MergeServerValue extends PlayerActivityIdListValue {
    /**
     * 签到记录 signRec[i] ---> 0: 未签， 1: 免费签到已签 2: 付费已签 3: both
     */
    private int[] signRec = new int[GameConstants.MergeServerActivityDays];
    /**
     * 活动期间累计充值金砖
     */
    private int rechargeCount;
    /**
     * 打折物品购买记录
     * id:times
     */
    private Map<Integer, Integer> discountRec = Maps.newConcurrentMap();
    /**
     * 金砖返利购买记录
     */
    private Map<Integer, Integer> giftBuyRec = Maps.newConcurrentMap();
    /**
     * 用户参与时记录一下等级
     */
    private int playerLv;

    public int getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public void incRechargeCount(int inc) {
        this.rechargeCount += inc;
    }

    public Map<Integer, Integer> getDiscountRec() {
        return discountRec;
    }

    public void incRec(int id) {
        discountRec.merge(id, 1, Integer::sum);
    }

    public int getSignRec(int i) {
        return signRec[i];
    }

    public void addSignRec(int i, int v) {
        signRec[i] += v;
    }

    public void addGiftBuyRec(int id, int cnt) {
        giftBuyRec.merge(id, cnt, Integer::sum);
    }

    public int getPlayerLv() {
        return playerLv;
    }

    public void setPlayerLv(int playerLv) {
        this.playerLv = playerLv;
    }

    @Override
    public void clearRepeatValue() {
        Arrays.fill(signRec, 0);
        rechargeCount = 0;
        discountRec.clear();
        giftBuyRec.clear();
        super.clearRepeatValue();
    }
}
