package com.hm.chsdk.event2.activity;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;

/**
 * 坦克改造-升星
 *
 * @author xb
 */
@EventMsg(obserEnum = ObservableEnum.TankStarUp)
public class TankStarUpEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        int tankId = (int) argv[0];
        if (tankId != 55) {
            return;//只要光令坦克
        }
        Tank tank = player.playerTank().getTank(tankId);
        if (tank == null || tank.getStar() != 5) {
            return;
        }
        loadEventType(CHEventType.Activity, 4004, "light_5star");
    }
}
