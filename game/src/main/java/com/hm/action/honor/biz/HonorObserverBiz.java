package com.hm.action.honor.biz;

import com.hm.config.GameConstants;
import com.hm.libcore.annotation.Biz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.util.RandomUtils;

import javax.annotation.Resource;

@Biz
public class HonorObserverBiz implements IObserver{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private PlayerBiz playerBiz;
	@Resource
    private GuildTechBiz guildTechBiz;
	@Resource
    private TroopBiz troopBiz;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.CityBattleResult, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Pvp1v1Result, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerCityOccupy, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case CityBattleResult:
				calBattleResult(player, (int)argv[0], (int)argv[1],(boolean)argv[2],(WorldCity)argv[3],(String)argv[4]);
				break;
			case Pvp1v1Result:
				cal1v1Result(player, (boolean)argv[0]);
				break;
			case PlayerCityOccupy:
				doCityOccupy(player, (AtkDefType)argv[0]);
				break;
		}
	}
	
	private void doCityOccupy(Player player,AtkDefType winType) {
		if(winType != AtkDefType.ATK) {
			return;//只有进攻占领
		}
		int occupyHonor = commValueConfig.getCommValue(CommonValueType.HonorWarOccupy);
		playerBiz.addPlayerHonor(player, HonorType.OccourCity, occupyHonor, LogType.CityOccupy);
	}
	
	private void cal1v1Result(Player player,boolean isWin) {
		long addHonor = commValueConfig.getCommValue(CommonValueType.Pvp1v1Honor);
		playerBiz.addPlayerHonor(player, HonorType.Pvp1v1, addHonor, LogType.PvpFight);
	}
	
	private void calBattleResult(Player player,int killTankNum,int deathTankNum,boolean isWin,WorldCity worldCity,String troopId) {
		//计算功勋
		calContrReward(player, killTankNum, deathTankNum);
		//计算荣誉
		calHonorReward(player, killTankNum, isWin, deathTankNum,troopId);
		//计算杀敌排行
		if(killTankNum > 0) {
			HdLeaderboardsService.getInstance().updatePlayerRankForAdd(player, RankType.CityKillTank, killTankNum);
//			if(worldCity.getWorldType() == WorldType.Berlin) {
//				HdLeaderboardsService.getInstance().updatePlayerRankForAdd(player, RankType.BerlinKillTank, killTankNum);
//			}
		}
	}
	
	private void calContrReward(Player player,int killTankNum,int deathTankNum) {
		if(player == null) {
			return;
		}
		long totalContr = killTankNum*commValueConfig.getCommValue(CommonValueType.ContrWarKill)
				+ deathTankNum*commValueConfig.getCommValue(CommonValueType.ContrWarDeath);
		playerBiz.addPlayerCurrency(player, CurrencyKind.Contr, totalContr, LogType.WarReward);
	}
	
	private void calHonorReward(Player player,int killTankNum,boolean isWin,int deathTankNum,String troopId) {
		if(player == null) {
			return;
		}
		if(killTankNum > 0) {
			long killAdd = killTankNum*commValueConfig.getCommValue(CommonValueType.HonorWarKill);
			playerBiz.addPlayerHonor(player, HonorType.KillTank, killAdd, LogType.WarReward);
		}
		if(!isWin) {
			WorldTroop worldTroop = TroopServerContainer.of(player).getWorldTroop(troopId);
			if(worldTroop != null) {
				double rate = commValueConfig.getDoubleCommValue(CommonValueType.HonorWarDeath);

				long honor = GameConstants.calDeathHonor(worldTroop.getTroopCombat(player),rate);
				playerBiz.addPlayerHonor(player, HonorType.DeathTank, honor, LogType.WarReward);
			}
		}
	}
}
