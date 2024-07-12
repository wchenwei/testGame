package com.hm.chsdk.event2.tank;

import com.hm.model.player.Player;
import com.hm.model.tank.Tank;

/**
 * 坦克羁绊
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.FriendLv)
public class TankFriendLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        Tank tank = player.playerTank().getTank(tankId);
        if (tank != null) {
            lv = tank.getFetters();
        }

        event_id = "8008";
        event_name = "坦克羁绊";
    }

    /*
    坦克id
激活后羁绊星级
    * */
    private int lv;
}
