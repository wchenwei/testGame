package com.hm.action.battle.biz;

import com.hm.config.MissionConfig;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

@Biz
public class BattleBiz implements IObserver{
	@Resource
	private MissionConfig missionConfig;



	public void checkBattleOpen(Player player) {
		try {
			missionConfig.checkBattleOpen(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		int id = (int) argv[0];
		if (missionConfig.getBattleUnlockFunctions().contains(id)){
			checkBattleOpen(player);
		}
    }







}
