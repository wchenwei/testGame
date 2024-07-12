package com.hm.chsdk.event2.activity;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @description:卡池类型
 * @author: chenwei
 * @create: 2023/3/2
 **/
@EventMsg(obserEnum = ObservableEnum.CHAct4007)
public class DrawCardEvent extends CommonParamEvent {

    public String card_id;
    public String card_type;

    @Override
    public void init(Player player, Object... argv) {
        this.card_id = Convert.toStr(argv[0]);
        this.card_type = Convert.toStr(argv[1]);
        loadEventType(CHEventType.Activity, 4007, "draw_card");
    }
}
