package com.hm.action.function.biz;

import com.hm.config.PlayerFunctionConfig;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

@Biz
public class FunctionBiz implements IObserver{
	@Resource
	private PlayerFunctionConfig playerFunctionConfig;
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.FBFightWin, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.LOGIN, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MainTaskComplete, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		playerFunctionConfig.checkFunctionOpen(player);
	}

}
