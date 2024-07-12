package com.hm.chsdk.event2.activity;

import com.hm.libcore.spring.SpringUtil;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.ActivityType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @description: 领取挡位
 * @author: chenwei
 * @create: 2023/2/27
 **/
@EventMsg(obserEnum = ObservableEnum.CHAct4003)
public class GearGetEvent extends CommonParamEvent {
    public int huodong_id; // 活动id
    public int periods_id;// 期数
    public int module;// 模块
    public String day; // 第几天
    public String gear_type; // 挡位

    @Override
    public void init(Player player, Object... argv) {
        int acType = (int) argv[0];
        int id = (int) argv[1];
        int module = (int) argv[2];
        ActivityType activityType = ActivityType.getActivityType(acType);
        if (activityType == null) {
            return;
        }
        CHActiveEventBiz eventBiz = SpringUtil.getBean(CHActiveEventBiz.class);
        int gearType = eventBiz.getGearTypeModule0(player, activityType, id, module);
        if (gearType < 0) {
            return;
        }
        loadEventType(CHEventType.Activity, 4003, "gear_get");
        this.huodong_id = acType;
        this.gear_type = String.valueOf(gearType);
        this.module = module;
        this.periods_id = eventBiz.getActivityStage(player, activityType);
        this.day = eventBiz.getActivityDay(player, activityType, id, module);
    }


}
