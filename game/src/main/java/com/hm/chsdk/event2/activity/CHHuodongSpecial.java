package com.hm.chsdk.event2.activity;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.ActivityType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;


/**
 * 草花活动统计  档位选择
 */
@EventMsg(obserEnum = ObservableEnum.CHAct4010)
public class CHHuodongSpecial extends CommonParamEvent {
    private int huodong_id;


    @Override
    public void init(Player player, Object... argv) {
        ActivityType activityType = (ActivityType) argv[0];
        this.huodong_id = activityType.getType();
        loadEventType(CHEventType.Activity, 4010, "huodong_special");
    }
}
