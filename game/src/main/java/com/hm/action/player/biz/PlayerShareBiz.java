package com.hm.action.player.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

@Biz
public class PlayerShareBiz implements IObserver {
    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.DayFirstLogin, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        if (observableEnum == ObservableEnum.DayFirstLogin) {
            player.playerShare().dayReset();
        }

    }
}
