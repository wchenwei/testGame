package com.hm.action.guildwar.biz;

import com.google.common.collect.Sets;
import com.hm.libcore.annotation.Biz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.ClonePlayerFightTroop;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @Description: 战斗奖励
 * @author siyunlong  
 * @date 2018年11月23日 下午12:15:28 
 * @version V1.0
 */
@Biz
public class WarRewardBiz {
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private WorldBiz worldBiz;
	@Resource
    private PlayerBiz playerBiz;
	@Resource
    private ItemBiz itemBiz;

//	private void doPvp1v1WarReward(WorldCity worldCity,AbstractFightTroop troop,boolean isWin) {
//		Player player = worldBiz.getPlayerByFightTroop(worldCity, troop);
//		if(player != null) {
//			player.notifyObservers(ObservableEnum.Pvp1v1Result, isWin);
//			player.sendUserUpdateMsg();
//		}
//	}
	
	public void doPvp1v1WarResult(WorldCity worldCity,WarResult warResult,AbstractFightTroop win,AbstractFightTroop lose) {
		//计算城战奖励
		doGuildWarResult(worldCity, warResult);
		//1v1奖励
//		doPvp1v1WarReward(worldCity, win, true);
//		doPvp1v1WarReward(worldCity, lose, false);
	}
	
	public void doGuildWarResult(WorldCity worldCity,WarResult warResult) {
		UnitGroup winGroup = warResult.getWinUnitGroup();
		UnitGroup loseGroup = warResult.getLoseUnitGroup();
		AbstractFightTroop winTroop = winGroup.getFightTroop();
		AbstractFightTroop loseTroop = loseGroup.getFightTroop();
		int winKillTankNum = winGroup.getKillTankNum();
		int loseKillTankNum = loseGroup.getKillTankNum();
		Player winPlayer = worldBiz.getPlayerByFightTroop(worldCity, winTroop);
		Player losePlayer = worldBiz.getPlayerByFightTroop(worldCity, loseTroop);
		
		if(winPlayer != null) {
			AtkDefType type = winTroop.isAtk()?AtkDefType.ATK:AtkDefType.DEF;
			worldCity.getCityFight().addJoinPlayer(type, winPlayer.getId());
			//抛出事件
			winPlayer.notifyObservers(ObservableEnum.CityBattleResult, winKillTankNum,loseKillTankNum,true,worldCity,winTroop.getId(),loseTroop, winGroup.getWarDrops());
			//计算掉落
			itemBiz.addItem(winPlayer, winGroup.getWarDrops(), LogType.FightDropItem);
			if(winTroop.getFightType() == FightType.Pvp) {
				winPlayer.notifyObservers(ObservableEnum.Pvp1v1Result, true);
			}
			winPlayer.sendUserUpdateMsg(true);//延迟保存库
		}
		if(losePlayer != null) {
			AtkDefType type = loseTroop.isAtk()?AtkDefType.ATK:AtkDefType.DEF;
			worldCity.getCityFight().addJoinPlayer(type, losePlayer.getId());
			//抛出事件
			losePlayer.notifyObservers(ObservableEnum.CityBattleResult, loseKillTankNum,winKillTankNum,false,worldCity,loseTroop.getId(),winTroop, loseGroup.getWarDrops());
			//计算掉落
			itemBiz.addItem(losePlayer, loseGroup.getWarDrops(), LogType.FightDropItem);
			if(loseTroop.getFightType() == FightType.Pvp) {
				losePlayer.notifyObservers(ObservableEnum.Pvp1v1Result, false);
			}
			losePlayer.sendUserUpdateMsg(true);//延迟保存库
		}

		this.doCalTroopIsClone(winTroop, loseTroop);
	}

	private void doCalTroopIsClone(AbstractFightTroop winTroop, AbstractFightTroop loseTroop){
		try {
			boolean isCloneTroop = winTroop instanceof ClonePlayerFightTroop;
			if(!isCloneTroop){
				return;
			}
			//玩家镜像部队 杀死敌人
			Player player = ((ClonePlayerFightTroop) winTroop).getPlayer();
			if(player == null){
				return;
			}
			player.notifyObservers(ObservableEnum.ClonePlayerTroopWin, loseTroop);
			player.sendUserUpdateMsg(true);//延迟保存库
		}catch (Exception e){

		}
	}

	/**
	 * 计算助攻奖励
	 * @param worldCity
	 */
	public void calWinHonorReward(WorldCity worldCity,Player winPlayer,AtkDefType winType) {
		Set<Long> atkCityPlayerIds = Sets.newHashSet();
		if(winType == AtkDefType.ATK){
			// 获取攻占成功后 有部队在该城池的玩家
			atkCityPlayerIds = worldBiz.getAtkCityPlayerIds(worldCity);
		}
		Set<Long> joinPlayerIds = worldCity.getCityFight().getJoinPlayerIds(winType);

		Set<Long> allPlayerIds = Sets.newHashSet();
		allPlayerIds.addAll(atkCityPlayerIds);
		allPlayerIds.addAll(joinPlayerIds);
		//计算助攻奖励
		int addHonor = commValueConfig.getCommValue(CommonValueType.HonorWarAssists);
		for (long playerId: allPlayerIds){
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null){
				continue;
			}
			if(atkCityPlayerIds.contains(playerId)){
				player.notifyObservers(ObservableEnum.playerJoinOccupyCity, worldCity);
			}
			// 攻占玩家
			boolean isWinPlayer = winPlayer != null && Objects.equals(playerId, winPlayer.getId());
			if(isWinPlayer){
				player.notifyObservers(ObservableEnum.PlayerCityOccupy, winType,worldCity.getId());
			}else if(joinPlayerIds.contains(playerId)){
				playerBiz.addPlayerHonor(player, HonorType.AssistCity, addHonor, LogType.AssistCity);
				player.notifyObservers(ObservableEnum.AssistCity,winType,worldCity);
			}
			player.sendUserUpdateMsg();
		}
	}

}
