package com.hm.redis.troop;

import com.hm.action.troop.redis.PlayerTroopRedisDB;
import com.hm.config.excel.TankConfig;
import com.hm.enums.TankRareType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.troop.TankArmy;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class TroopRedisBiz implements IObserver{
	@Resource
    private TankConfig tankConfig;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeArenaTroop, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case TroopChange:
				doWorldTroopChange(player);
				break;
		}
	}

	
	public void doWorldTroopChange(Player player) {
		PlayerTroopRedisDB.saveRedis(player);//保存redis

		List<Integer> tankIds = TroopServerContainer.of(player).getWorldTroopByPlayer(player)
								.stream().flatMap(e -> e.getTroopArmy().getTankList().stream())
								.map(e -> e.getId())
								.filter(e -> isSsTank(e)).collect(Collectors.toList());
		TroopRedisType.World.create().build(player.getId(), tankIds).saveRedis();
	}
	
	public void changeRedisTroop(TroopRedisType type,long playerId,List<TankArmy> tankList) {
		List<Integer> tankIds = tankList.stream().map(e -> e.getId())
				.filter(e -> isSsTank(e)).collect(Collectors.toList());
		type.create().build(playerId, tankIds).saveRedis();
	}
	
	
	public boolean isSsTank(int tankId) {
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		return tankSetting != null && tankSetting.getRare() == TankRareType.SSR.getType();
	}
}
