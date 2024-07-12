package com.hm.chsdk.event2.battle;

import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;

/**
 * @author wyp
 * @description
 * @date 2022/3/9 9:26
 */
public class BattleEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        super.event_type_id = "011";
        super.event_type_name = "出征基地";
        this.playerId = player.getId();
    }

    private long playerId;

}
