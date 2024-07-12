package com.hm.chsdk.event2.activity;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.ActivityType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName ActTreasureEvent
 * @Deacription 活动投资理财
 * @Author zxj
 * @Date 2023-03-02 10:28
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.ActTreasure)
public class ActTreasureEvent extends CommonParamEvent {

    private int huodong_id; //活动id
    private int invest_type; //投资档位

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Activity, 4014, "invest");
        ActivityType actType = (ActivityType) argv[0];
        huodong_id = actType.getType();
        invest_type = (int) argv[1];
    }
}
