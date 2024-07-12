package com.hm.chsdk.event2.battle;

import com.hm.model.player.Player;

/**
 * @author wyp
 * @description 使命之路普通扫荡
 * @date 2022/3/10 10:16
 */
//@EventMsg(obserEnum = ObservableEnum.TowerBattleEvent)
public class TowerBattleEvent extends BattleEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        super.event_id = "11009";
        super.event_name = "使命之路普通扫荡";
        this.endId = (int) argv[0];
    }

    private int endId;
}
