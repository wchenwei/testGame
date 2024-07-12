package com.hm.model.activity.payeveryday;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.activity.PayEveryDayBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.model.activity.PlayerActivityValue;
import com.hm.model.player.BasePlayer;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClassName: PayEveryDayValue. <br/>
 * Function: 每日必买活动. <br/>
 * date: 2019年6月12日 下午5:51:57 <br/>
 * @author zxj
 * @version
 */
public class PayEveryDayValue extends PlayerActivityValue{

    /**
     * giftId -> today buy times
     */
	private Map<Integer, Integer> buyState = Maps.newHashMap();
	/**
	 * 局座点数
	 */
	private int point;
	/**
	 * 局座点数兑换记录，ActiveDailyRechargePointsTemplateImpl::getId
	 */
	private List<Integer> exchangeRec = Lists.newArrayList();

    // 剩余领取天数
    @Getter
    private Map<Integer, Integer> dayMap = Maps.newConcurrentMap();

    //---------------------改版--
    /**
     * 已购买的剩余可领取的
     * giftId -> count
     */
    @Getter
    private Map<Integer, Integer> rewardMap = Maps.newConcurrentMap();
    /**
     * 已购买7天用户，当日领取gift id记录
     */
    @Getter
    private Set<Integer> todayReward = Sets.newConcurrentHashSet();
    //-----------------------

	@Override
	public boolean checkCanReward(BasePlayer player, int id) {
        if (buyState.containsKey(id) || todayReward.contains(id)) {
            return false;
        }
        // 旧版本剩余数据处理
        if (dayMap.getOrDefault(id, 0) > 0) {
			return true;
		}
        if (rewardMap.getOrDefault(id, 0) > 0) {
            return true;
        }
		return false;
	}

    public boolean isOldVersion(int id) {
        return dayMap.getOrDefault(id, 0) > 0;
    }

	@Override
    public void setRewardState(BasePlayer player, int id) {
        // 扣已购买次数
        boolean old = dayMap.getOrDefault(id, 0) > 0;
        if (old) {
            dayMap.merge(id, -1, Integer::sum);
            if (dayMap.get(id) == 0) {
                dayMap.remove(id);
            }
        } else {
            rewardMap.merge(id, -1, Integer::sum);
        }
        // 加point
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        // 单倍奖励给的point
        int payEveryPoint = commValueConfig.getPayEveryPoint(id);
        // 新版本一次性领取所有倍数,对于的point也根据倍数
        int p = old ? payEveryPoint : payEveryPoint * PayEveryDayBiz.MAX_TIMES;
        incPoint(p);

        todayReward.add(id);
        player.getPlayerActivity().SetChanged();
    }

    public void buyReward(BasePlayer player, int id) {
        // 记录积分
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        int payEveryPoint = commValueConfig.getPayEveryPoint(id);
        this.incPoint(payEveryPoint);
        buyState.merge(id, 1, Integer::sum);
        player.getPlayerActivity().SetChanged();
    }

    public boolean dayReset() {
        // is change
        boolean c = false;
        if (!buyState.isEmpty()) {
            buyState.clear();
            c = true;
        }
        if (!todayReward.isEmpty()) {
            todayReward.clear();
            c = true;
        }
        return c;
	}

	public void incPoint(int inc) {
		point += inc;
	}

	public int getPoint() {
		return point;
	}

	public List<Integer> getExchangeRec() {
		return exchangeRec;
	}

    public void addDays(List<Integer> payEveryIds) {
        //购买7天计费点当天清空当天单独购买1、3、6的记录
        if (rewardMap.values().stream().noneMatch(n->n>0)) {
            buyState.clear();
        }
        for (Integer giftId : payEveryIds) {
            rewardMap.merge(giftId, 7, Integer::sum);
        }
    }

    public void operationDay(int giftId, int num) {
        dayMap.merge(giftId, -num, Integer::sum);
    }

    public int getDays(int giftId) {
        return dayMap.getOrDefault(giftId, 0);
    }


    public Map<Integer, Integer> getBuyState() {
        return buyState;
    }

    public boolean check() {
        return dayMap.values().stream().anyMatch(surplus -> surplus > 0) || rewardMap.values().stream().anyMatch(n->n > 0);
	}
}




