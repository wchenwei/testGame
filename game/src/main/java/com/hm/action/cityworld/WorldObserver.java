package com.hm.action.cityworld;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.config.GameConstants;
import com.hm.enums.WorldType;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.world.WorldServerContainer;

import javax.annotation.Resource;

@Biz
public class WorldObserver implements IObserver{

	@Resource
	private WorldBiz worldBiz;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildChangeNameOrFlag, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MinuteEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case GuildChangeNameOrFlag:
				doGuildChangeFlag((Guild) argv[0]);
				break;
			case MinuteEvent:
				doMinuteEvent((int)argv[0]);
				break;
		}
	}
	
	public void doGuildChangeFlag(Guild guild) {
		WorldServerContainer.of(guild).getWorldCitys(WorldType.Normal)
			.stream().filter(e -> e.getCityBelong().getGuildId() == guild.getId())
			.forEach(worldCity ->  {
				worldBiz.broadWorldCityUpdate(worldCity);
			});
	}
	
	public void doMinuteEvent(int minute) {
		int hour = DateUtil.thisHour(true);
		if(minute <= 1 && hour == 20 || minute >= 59 && hour == 19) {
			//在晚上20点整点期间，对城市锁加时，防止出现多线程改变城池
			GameConstants.CityLockSecond = 3;
			return;
		}
		GameConstants.CityLockSecond = 1;
	}
}
