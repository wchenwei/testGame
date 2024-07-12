package com.hm.action.troop.biz;


import com.hm.config.excel.CommanderConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.TroopState;
import com.hm.libcore.annotation.Biz;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.war.sg.troop.ClonePlayerFightTroop;

import javax.annotation.Resource;

@Biz
public class TroopObserver implements IObserver{
	@Resource
    private TroopBiz troopBiz;
	@Resource
    private CommanderConfig commanderConfig;
	
	@Override
	public void registObserverEnum() {
//		ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeName, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerQuit, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CloneTroopDeath, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
//			case ChangeName:
//				doChangeName(player);
//				break;
			case GuildPlayerAdd:
			case GuildPlayerQuit:
				checkGuildForWorldTroop(player);
				break;
			case CloneTroopDeath:
				doCloneTroopDeath((ClonePlayerFightTroop)argv[0]);
				break;
		}
	}
	
//	private void doChangeName(Player player) {
//		List<WorldTroop> troopList = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
//		for (WorldTroop worldTroop : troopList) {
//			worldTroop.getTroopInfo().setName(player.getName());
//			worldTroop.saveDB();
//		}
//	}
	
	/**
	 * 玩家离开部落处理部队
	 * @param player
	 */
	public void checkGuildForWorldTroop(Player player) {
		//把所有空闲部队离开原来的城池
		TroopServerContainer.of(player).getWorldTroopByPlayer(player).stream()
				.filter(worldTroop -> TroopState.isIdleState(worldTroop.getState()))
				.forEach(worldTroop -> {
					WorldCity worldCity = WorldServerContainer.of(worldTroop).getWorldCity(worldTroop.getCityId());
					if(worldCity != null) {
						worldCity.removeTroop(worldTroop.getId());
						worldCity.saveDB();
					}
					troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
				});
	}
	
	public void doCloneTroopDeath(ClonePlayerFightTroop clonePlayerFightTroop) {
		Player player = PlayerUtils.getPlayer(clonePlayerFightTroop.getPlayerId());
		long exp = commanderConfig.getMilitaryExtraTemplate(player).getGhost_reward();

		boolean isSuc = player.playerCloneTroops().deathCityTroop(clonePlayerFightTroop.getCityId(), exp);
		if(isSuc) {
			player.sendUserUpdateMsg();
		}
	}

}











