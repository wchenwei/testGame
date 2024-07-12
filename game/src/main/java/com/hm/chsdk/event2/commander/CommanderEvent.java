package com.hm.chsdk.event2.commander;

import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;


/**
 * @ClassName CommanderEvent
 * @Deacription CommanderEvent
 * @Author zxj
 * @Date 2022/3/11 9:28
 * @Version 1.0
 **/
public abstract class CommanderEvent extends CommonParamEvent {
    @Override
    public void init(Player player, Object... argv) {
        this.event_type_id="009";
        this.event_type_name="指挥官";

        this.initCommanderData(player, argv);
    }

    public abstract void initCommanderData(Player player, Object... argv);
}
