package com.hm.chsdk.event2.pvp;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 荣誉统计
 * @date 2023/2/27 15:50
 */
@EventMsg(obserEnum = ObservableEnum.CHHonorStat)
public class CHHonorEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        honour = player.playerHonor().getTotalHonor();
        if (honour > 0) {
            loadEventType(CHEventType.PVP, 6006, "city_honour");
        }
    }

    long honour;
}
