package com.hm.action.push.observer;


import com.hm.config.excel.ActivityConfig;
import com.hm.container.PushContainer;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;

@Biz
public class PushObserver implements IObserver{
	@Resource
    private PushContainer pushContainer;
	@Resource
    private ActivityConfig activityConfig;
	
	@Override
	public void registObserverEnum() {
//		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
//		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildWarHerald, this);
//		ObserverRouter.getInstance().registObserver(ObservableEnum.HangUpEnd, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
//		switch (observableEnum) {
//			case HourEvent:
//				checkOil((int)argv[0]);
//				break;
//			case HangUpEnd:
//				PushUtil.sendMessage(player, PushType.TroopHangUpEnd);
//				break;
//		}
	}
	
	private void checkOil(int hour) {
//		if(activityConfig.getArmyFeteStartHours().contains(hour)) {
//			pushContainer.broadPush(PushType.HourReward);
//		}
	}

}











