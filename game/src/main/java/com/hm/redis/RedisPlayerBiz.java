package com.hm.redis;

import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.util.RedisUtil;

@Biz
public class RedisPlayerBiz implements IObserver{
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeName, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopCombatChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerAdd, this,0);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerQuit, this,0);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeFrameIcon, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeIcon, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildChangeJob, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeTitle, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CampOfficialChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.VipLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.CarLv, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ComposeEqu, this);
	}
	
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		RedisUtil.updateRedisPlayer(player);
	}
}
