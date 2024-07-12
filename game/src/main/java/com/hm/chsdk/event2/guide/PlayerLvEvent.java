package com.hm.chsdk.event2.guide;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

@EventMsg(obserEnum = ObservableEnum.PlayerLevelChange)
public class PlayerLvEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Task, 3003, "role_upgrade");
        this.role_level = player.playerLevel().getLv();
    }

    public int role_level;
}
