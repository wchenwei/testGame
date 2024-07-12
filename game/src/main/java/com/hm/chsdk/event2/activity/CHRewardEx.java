package com.hm.chsdk.event2.activity;

import cn.hutool.core.convert.Convert;
import com.hm.libcore.msg.JsonMsg;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.ActivityType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.Objects;


/**
 * 草花活动统计  档位选择
 */
@EventMsg(obserEnum = ObservableEnum.CHAct4006)
public class CHRewardEx extends CommonParamEvent {
    private int huodong_id;
    // 兑换道具id
    private int prop_id;
    private long prop_num;
    private String huodong_discount;

    @Override
    public void init(Player player, Object... argv) {
        ActivityType activityType = (ActivityType) argv[0];
        if (activityType == null) {
            return;
        }
        this.huodong_id = activityType.getType();
        Items items = (Items) argv[1];
        if (Objects.nonNull(items)) {
            this.prop_id = items.getId();
            this.prop_num = items.getCount();
        }
        switch (activityType) {

//            case DisCount:
//                this.huodong_discount = Convert.toStr(argv[2]);
//                break;
//                default:
//                return;
        }
//        loadEventType(CHEventType.Activity, 4006, "reward_ex");
    }


}
