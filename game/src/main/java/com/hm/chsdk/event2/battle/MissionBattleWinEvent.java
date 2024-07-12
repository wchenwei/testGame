package com.hm.chsdk.event2.battle;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName MissionBattleWinEvent
 * @Deacription 主线副本胜利
 * @Author zxj
 * @Date 2023-02-27 17:44
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.FBFightWin)
public class MissionBattleWinEvent extends MissionBattleEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        this.setFightResult(true);
    }
}
