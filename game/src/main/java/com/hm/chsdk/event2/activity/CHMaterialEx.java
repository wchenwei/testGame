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
@EventMsg(obserEnum = ObservableEnum.CHAct4009)
public class CHMaterialEx extends CommonParamEvent {
    private int huodong_id;
    // 兑换道具id
    private int prop_id;

    private long prop_num;

    @Override
    public void init(Player player, Object... argv) {
        ActivityType activityType = (ActivityType) argv[0];
        this.huodong_id = activityType.getType();
        switch (activityType) {

            default:
                return;
        }
//        loadEventType(CHEventType.Activity, 4009, "material_ex");
    }


}
