package com.hm.chsdk.event2.pvp;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.KfType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 荣誉统计
 * @date 2023/2/27 15:50
 */
@EventMsg(obserEnum = ObservableEnum.CHKFPlayerOil)
public class CHKFOilEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        this.cross_id = ((KfType) argv[0]).getType();
        loadEventType(CHEventType.PVP, 6003, "cross_transport");
    }

    private int cross_id;

}
