package com.hm.servercontainer.worldbuild;

import com.google.common.collect.Maps;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class WorldBuildTroopItemContainer extends ItemContainer{
	//玩家部队map key:部队id  value:部队
	private Map<Long,PlayerWorldBuild> playerMap = Maps.newConcurrentMap();
	
	public WorldBuildTroopItemContainer(int serverId) {
		super(serverId);
	}
	
	@Override
	public void initContainer() {
		for (PlayerWorldBuild playerWorldBuild : RedisMapperUtil.queryListAll(getServerId(),PlayerWorldBuild.class)) {
			this.playerMap.put(playerWorldBuild.getId(), playerWorldBuild);
		}
	}
	
	public void addSupplyTroop(PlayerWorldBuild playerWorldBuild) {
		this.playerMap.put(playerWorldBuild.getId(), playerWorldBuild);
		playerWorldBuild.saveDB();
	}
	
	public PlayerWorldBuild getPlayerWorldBuild(long playerId) {
		return this.playerMap.get(playerId);
	}
	public void removeSupplyTroop(long playerId) {
		PlayerWorldBuild supplyTroop = this.playerMap.remove(playerId);
		if(supplyTroop != null) {
//			getMongodDB().remove(supplyTroop);
			supplyTroop.delete();
		}
	}
	
	/**
	 * 部队是否在采集中
	 * @param worldTroop
	 * @return
	 */
	public boolean troopIsmMining(WorldTroop worldTroop) {
		PlayerWorldBuild playerWorldBuild = getPlayerWorldBuild(worldTroop.getPlayerId());
		if(playerWorldBuild == null) {
			return false;
		}
		WorldBuildTroop worldBuildTroop = playerWorldBuild.getWorldBuildTroop(worldTroop.getId());
		if(worldBuildTroop == null || worldBuildTroop.isOver()) {
			return false;
		}
		return true;
	}

	public Map<Long, PlayerWorldBuild> getPlayerMap() {
		return playerMap;
	}
	
	/**
	 * 空所有数据
	 */
	public void clearData() {
		this.playerMap.clear();
		RedisMapperUtil.deleteAll(getServerId(), PlayerWorldBuild.class);
	}
}
