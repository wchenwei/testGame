package com.hm.redis.tank;

import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

@Biz
public class RedisTankBindObserver implements IObserver{

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.SoldierBind, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.SoldierUnBind, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		new PlayerTankBind(player).saveRedis();
	}

}
