package com.hm.action.cityworld.biz;

import com.google.common.collect.Sets;
import com.hm.action.cityworld.vo.WorldCityVo;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommanderConfig;
import com.hm.config.excel.RandomTaskConfig;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.CityTroopType;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.cityworld.troop.ClonePlayerTroop;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class WorldCityBiz {
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private RandomTaskConfig randomTaskConfig;
	@Resource
	private CommanderConfig commanderConfig;

	
	public void clearWorldCity(int serverId) {
		List<Player> playerList = PlayerContainer.getOnlinePlayersByServerId(serverId);
		//给当前所有玩家发送战斗结束
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_BroadWorldCityFightResulte);
		playerList.stream().filter(e -> e.playerTemp().getCurCityId() > 0).forEach(e -> e.sendMsg(msg));
		//清空所有城池
		Set<Long> clonePlayerIds = Sets.newHashSet();
		WorldServerContainer.of(serverId).getWorldCitys(WorldType.Normal).stream()
		.forEach(worldCity -> {
			try {
				worldCity.lockWrite();
				worldBiz.clearTankRetainState(worldCity);
				worldCity.getCityFight().clearFightData();
				//找出存活的clone部队
				clonePlayerIds.addAll(worldCity.getAtkCityTroop().getClonePlayerIds());
				clonePlayerIds.addAll(worldCity.getDefCityTroop().getClonePlayerIds());

				worldCity.getAtkCityTroop().clearTroop();
				worldCity.getDefCityTroop().clearTroop();
				worldCity.getCityStatus().clearData();
				worldBiz.resetNpc(worldCity);//重新生成npc
			} finally{
				worldCity.unlockWrite();
			}
		});
		//所有部队移到空中
		TroopServerContainer.of(serverId).getPlayerTroopMap().values()
		.stream().filter(e -> e.getCityId() != GameConstants.PeaceId)
		.forEach(worldTroop -> troopBiz.movePlayerTroopToBerlin(worldTroop));

		//更新所有玩家城池列表更新
		List<WorldCityVo> cityList = WorldServerContainer.of(serverId).getWorldCitys(WorldType.Normal)
				.stream().map(e -> worldBiz.createWorldCityVo(e)).collect(Collectors.toList());
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_IntoWorld);
		serverMsg.addProperty("cityList", cityList);
		playerList.stream().forEach(player -> {
			player.sendWorldTroopMessage();//更新部队
			player.sendMsg(serverMsg);
		});

		//clone部队处理
		doPlayerCloneTroopAllDeath(clonePlayerIds);
	}
	
	/**
	 * 计算城战额外属性加成
	 * @param player
	 * @param cityId
	 * @return
	 */
	public Map<Integer,Double> calWorldCityAttr(Player player,int cityId) {
		if(cityId <= 0) {
			return new HashMap<>();
		}
		//计算地图民情亲密度属性加成
		int score = player.playerRandomTask().getCityValue(cityId);
		return randomTaskConfig.getEventBuffAttr(score);
	}


	/**
	 * @author siyunlong
	 * @version V1.0
	 * @Description: 放弃城池
	 * @date 2024/3/30 15:12
	 */
	public void giveUpCity(Player managerPlayer, WorldCity worldCity) {
		Set<Long> clonePlayerIds = Sets.newHashSet();

		try {
			log.error(managerPlayer.getId()+"->"+managerPlayer.getGuildId()+"放弃城池:"+worldCity.getId());
			worldCity.lockWrite();
			int guildId = managerPlayer.getGuildId();
			worldBiz.clearTankRetainState(worldCity);
			worldCity.getCityBelong().setGuildId(0);
			worldCity.getCityFight().clearFightData();
			//部队移动到攻击方
			worldCity.getDefCityTroop().removeAllNpc();
			worldBiz.moveWinTroopToDef(worldCity,0);
			//把己方部队所有都飘在空中

			List<BaseCityFightTroop> troopList = worldCity.getAtkCityTroop().getTroopList();
			troopList.removeIf(temp -> {
				if(temp.getTroopType() == CityTroopType.PlayerTroop.getType()) {
					WorldTroop worldTroop = TroopServerContainer.of(managerPlayer).getWorldTroop(temp.getId());
					if(worldTroop != null) {
						Player player = PlayerUtils.getPlayer(worldTroop);
						if(player.getGuildId() == guildId) {
							worldTroop.changeState(TroopState.None);
							worldTroop.setCityId(0);//飘在空中
							worldTroop.saveDB();
							troopBiz.sendWorldTroopUpdate(player, worldTroop);
							return true;
						}
					}
				}else if(temp.getTroopType() == CityTroopType.ClonePlayer.getType()) {
					ClonePlayerTroop clonePlayerTroop = (ClonePlayerTroop)temp;
					Player player = PlayerUtils.getPlayer(clonePlayerTroop.getPlayerId());
					if(player.getGuildId() == guildId) {
						clonePlayerIds.add(player.getId());//clone部队直接死亡
						return true;
					}
				}
				return false;
			});
			worldCity.getCityStatus().clearData();
			worldBiz.resetNpc(worldCity);//重新生成npc

			worldBiz.broadWorldCityUpdateAndTroopSize(worldCity);//广播城池变化

			ObserverRouter.getInstance().notifyObservers(ObservableEnum.GiveUpCity, managerPlayer, worldCity);
		} finally{
			worldCity.unlockWrite();
		}

		//此城池的clone部队死亡
		doPlayerCloneTroopCityDeath(worldCity.getId(),clonePlayerIds);
	}


	//玩家clone部队全部死亡处理
	public void doPlayerCloneTroopAllDeath(Set<Long> clonePlayerIds) {
		for (long playerId : clonePlayerIds) {
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				continue;
			}
			long exp = commanderConfig.getMilitaryExtraTemplate(player).getGhost_reward();
			player.playerCloneTroops().deathAllCityTroop(exp);
			if(player.playerCloneTroops().Changed()) {
				player.sendUserUpdateMsg();
			}
		}
	}
	public void doPlayerCloneTroopCityDeath(int cityId,Set<Long> clonePlayerIds) {
		for (long playerId : clonePlayerIds) {
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				continue;
			}
			long exp = commanderConfig.getMilitaryExtraTemplate(player).getGhost_reward();
			player.playerCloneTroops().deathCityAllTroop(cityId,exp);
			if(player.playerCloneTroops().Changed()) {
				player.sendUserUpdateMsg();
			}
		}
	}
}
