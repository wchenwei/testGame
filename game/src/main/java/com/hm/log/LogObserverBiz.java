package com.hm.log;

import com.hm.libcore.annotation.Biz;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
@Biz
public class LogObserverBiz implements IObserver{

	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.OccupyCity, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch(observableEnum) {
			case OccupyCity :
				logOccupyCity((WorldCity)argv[0],(int)argv[1],(boolean)argv[2]);
				break;
		}
	}
	
	public void logOccupyCity(WorldCity worldCity,int oldGuildId,boolean isMainCity) {
		if(isMainCity) {
			log.error(worldCity.getServerId()+"_"+oldGuildId+"主城被攻占:"+worldCity.getBelongGuildId());
		}
	}

	
}
