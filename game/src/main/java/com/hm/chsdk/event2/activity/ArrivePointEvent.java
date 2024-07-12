package com.hm.chsdk.event2.activity;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @description: 活动到达节点
 * @author: chenwei
 * @create: 2023/3/2
 **/
@EventMsg(obserEnum = ObservableEnum.CHAct4013)
public class ArrivePointEvent extends CommonParamEvent {
    public int huodong_id;
    public int point_id;


    @Override
    public void init(Player player, Object... argv) {
        this.huodong_id = Convert.toInt(argv[0]);
        this.point_id = Convert.toInt(argv[1]);
        loadEventType(CHEventType.Activity, 4013, "arrive_point");
    }
}
