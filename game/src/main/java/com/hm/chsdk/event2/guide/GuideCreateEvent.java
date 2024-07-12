package com.hm.chsdk.event2.guide;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * guide_completed_start
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/11/14 16:45
 */
@EventMsg(obserEnum = ObservableEnum.CHPlayerStep)
public class GuideCreateEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Task, 3002, "guide_completed_start");
        this.step = (int) argv[0];
    }

    public int step;
}
