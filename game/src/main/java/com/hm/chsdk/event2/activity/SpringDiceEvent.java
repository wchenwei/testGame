package com.hm.chsdk.event2.activity;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @description: 节日踏青骰子
 * @author: chenwei
 * @create: 2023/3/2
 **/
@EventMsg(obserEnum = ObservableEnum.CHAct4011)
public class SpringDiceEvent extends CommonParamEvent {
    public String dice_type;

    @Override
    public void init(Player player, Object... argv) {
        this.dice_type = Convert.toStr(argv[0]);
        loadEventType(CHEventType.Activity, 4011, "spring_dice");
    }
}
