package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克乘员装配
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHPassengerUp)
public class TankPassengerUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        uids = Convert.toStr(argv[1]);

        event_id = "8020";
        event_name = "坦克乘员装配";
    }

    /*
退役乘员ids
     */
    private String uids;
}
