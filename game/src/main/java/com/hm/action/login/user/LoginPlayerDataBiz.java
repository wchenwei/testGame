package com.hm.action.login.user;

import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

/**
 * 登录服用的玩家信息
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/9/9 17:08
 */
@Biz
public class LoginPlayerDataBiz implements IObserver {
    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeName, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerCreate, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginSuc, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        LoginPlayerData.update(player);
    }
}
