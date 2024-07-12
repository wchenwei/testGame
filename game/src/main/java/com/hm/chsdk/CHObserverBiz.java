package com.hm.chsdk;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.chsdk.event.HeartChEvent;
import com.hm.chsdk.event.InitChEvent;
import com.hm.chsdk.event.LoginChEvent;
import com.hm.chsdk.event.RechargeChEvent;
import com.hm.config.GameConstants;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import java.util.Map;


@Biz
public class CHObserverBiz implements IObserver{
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLoginSuc, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerHeart, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.RechargeSdk, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		if(observableEnum == ObservableEnum.HourEvent) {
			doHourEvent();
			return;
		}
		if(player == null || CHSDKContants.SDKClose) {
			return;
		}
		ChSDKConf chSDKConf = ChSDKConfUtils.getChSDKConf(player.getChannelId());
		if(chSDKConf == null || !chSDKConf.isSendCH1()) {
			return;
		}
		switch (observableEnum) {
			case PlayerLoginSuc:
				doPlayerLogin(player,chSDKConf);
				break;
			case PlayerHeart:
				doPlayerHeart(player,chSDKConf);
				break;
			case RechargeSdk:
				doPlayerRecharge(player,chSDKConf,(Map<String, String>)argv[0]);
				break;
		}
	}
	
	public void doPlayerLogin(Player player,ChSDKConf chSDKConf) {
		new InitChEvent(player,chSDKConf).send();
		new LoginChEvent(player,chSDKConf).send();
	}
	
	public void doPlayerHeart(Player player,ChSDKConf chSDKConf) {
		if(System.currentTimeMillis() - player.playerTemp().getLastCHHeartTime()
				> 5*GameConstants.MINUTE) {
			new HeartChEvent(player,chSDKConf).send();
			player.playerTemp().setLastCHHeartTime(System.currentTimeMillis());
		}
	}
	
	public void doPlayerRecharge(Player player,ChSDKConf chSDKConf,Map<String, String> params) {
		new RechargeChEvent(player, chSDKConf, params).send();
	}
	
	public void doHourEvent() {
		if(DateUtil.thisHour(true) > 0) {
			ChSDKConfUtils.init();
		}
	}
}
