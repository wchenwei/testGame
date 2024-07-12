package com.hm.chsdk.event2.activity;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;


@EventMsg(obserEnum = ObservableEnum.ChInviteReceive)
public class ChInviteReward extends CommonParamEvent {
    private int gear_type;

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Share, 8002, "share_gift");
        this.gear_type = (int)argv[0];
    }
}
