package com.hm.redis.observer;

import com.hm.libcore.annotation.Biz;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.util.RedisUtil;

@Biz
public class RedisGuildObserver implements IObserver{

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildCreate, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildChangeNameOrFlag, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildUpdateLv, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		Guild guild = (Guild) argv[0];
		RedisUtil.updateRedisGuild(guild);
	}

}
