package com.hm.chsdk.event2.register;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * 第一次改名
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/11/14 16:56
 */
@EventMsg(obserEnum = ObservableEnum.ChangeName)
public class FirstNameEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        if (player.getPlayerStatistics().getLifeStatistics(StatisticsType.ReNameCount) > 0) {
            return;
        }
        loadEventType(CHEventType.register, 1010, "enter_name");
    }

}
