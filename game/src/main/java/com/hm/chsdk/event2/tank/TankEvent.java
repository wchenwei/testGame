package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;

public abstract class TankEvent extends CommonParamEvent {
    public int tankId;

    @Override
    public void init(Player player, Object... argv) {
        event_type_id = "008";
        event_type_name = "坦克相关";
        tankId = Convert.toInt(argv[0]);
    }
}
