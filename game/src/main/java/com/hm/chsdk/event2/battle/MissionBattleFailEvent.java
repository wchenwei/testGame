package com.hm.chsdk.event2.battle;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName MissionBattleFailEvent
 * @Deacription 主线副本失败
 * @Author zxj
 * @Date 2023-02-27 17:48
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.MainFbFail)
public class MissionBattleFailEvent extends MissionBattleEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        this.setFightResult(false);
    }

}
