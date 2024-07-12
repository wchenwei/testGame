package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克乘员退役
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHPassengerRetire)
public class TankPassengerRetireEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, 0);

        uids = Convert.toStr(argv[0]);

        event_id = "8024";
        event_name = "坦克乘员退役";
    }

    /*
退役乘员id
     */
    private String uids;
}
