package com.hm.chsdk.event2.activity;

import com.hm.config.ContinuousPurchaseConfig;
import com.hm.config.excel.*;
import com.hm.config.excel.temlate.ActiveRechargeWeeklyTemplate;
import com.hm.config.excel.templaextra.ActiveConsumeCarnivalTemplate;
import com.hm.config.excel.templaextra.ActiveRechargeCarnivalTemplate;
import com.hm.enums.ActivityType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityServerContainer;

import javax.annotation.Resource;

/**
 * @description:
 * @author: chenwei
 * @create: 2023/2/27
 **/
@Biz
public class CHActiveEventBiz {

    @Resource
    private ActivitySimpleConfig activitySimpleConfig;
    @Resource
    private ContinuousPurchaseConfig continuousPurchaseConfig;
    @Resource
    private ZeroGiftConfig zeroGiftConfig;
    @Resource
    private ActivityConfig activityConfig;
    @Resource
    private Double11FestivalConfig festivalConfig;
    @Resource
    private ActiveArmyRaceConfig armyRaceConfig;

    @Resource
    private Active121Config active121Config;
    @Resource
    private Active97Config active97Config;
    @Resource
    private Activity0801Config activity0801Config;

    public int getGearTypeModule0(Player player, ActivityType activityType, int id, int module) {
        switch (activityType) {
            default:
                return -1;
        }
    }

    public int getActivityStage(Player player, ActivityType activityType) {
        AbstractActivity abstractActivity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
        switch (activityType) {
            default:
                return 0;
        }
    }

    public String getActivityDay(Player player, ActivityType activityType, int id, int module) {
        AbstractActivity abstractActivity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
        int day;
        switch (activityType) {
            default:
                day = abstractActivity.getDays();
                break;
        }
        return day <= 0 ? null : String.valueOf(day);
    }
}
