package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克乘员培养-训练
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.Passenger_LvUp)
public class TankPassengerLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        id = Convert.toInt(argv[1]);
        oldLv = Convert.toInt(argv[2]);
        newLv = Convert.toInt(argv[3]);

        event_id = "8021";
        event_name = "坦克乘员培养-训练";
    }

    /*
    坦克id
乘员id
训练前乘员等级
训练后乘员等级
     */
    private int id;
    private int oldLv;
    private int newLv;
}
