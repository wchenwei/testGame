package com.hm.servercontainer.troop;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class TroopItemContainer extends ItemContainer{
	//玩家部队map key:部队id  value:部队
	private Map<String,WorldTroop> playerTroopMap = Maps.newConcurrentMap();
	
	public TroopItemContainer(int serverId) {
		super(serverId);
	}
	
	@Override
	public void initContainer() {
		for (WorldTroop worldTroop : getWorldTroopFromDB()) {
			this.playerTroopMap.put(worldTroop.getId(), worldTroop);
			if(worldTroop.getState() == TroopState.PvpOneByOne.getType()) {
				worldTroop.changeState(TroopState.None);
			}
			if(worldTroop.getWorldType() == WorldType.KF.getType()) {
				worldTroop.changeState(TroopState.None);
				worldTroop.setCityId(0);
				worldTroop.saveDB();
			}
		}
	}
	
	private List<WorldTroop> getWorldTroopFromDB() {
//		List<WorldTroop> troopList = WorldTroopRedisUtils.getWorldTroop(getServerId());
//		if(CollUtil.isNotEmpty(troopList)) {
//			return troopList;
//		}
//		List<WorldTroop> oldTroops = getMongodDB().queryAll(WorldTroop.class);
//		if(CollUtil.isEmpty(oldTroops)) {
//			return troopList;
//		}
//		WorldTroopRedisUtils.updateWorldTroopList(getServerId(), oldTroops);
		return RedisMapperUtil.queryListAll(getServerId(), WorldTroop.class);
	}
	
	public void addWorldTroop(WorldTroop worldTroop) {
		this.playerTroopMap.put(worldTroop.getId(), worldTroop);
		worldTroop.saveDB();
	}
	
	public WorldTroop getWorldTroop(String id) {
		if(StrUtil.isEmpty(id)) {
			return null;
		}
		return this.playerTroopMap.get(id);
	}
	public void removeWorldTroop(String id) {
		WorldTroop worldTroop = this.playerTroopMap.remove(id);
		if(worldTroop != null) {
//			getMongodDB().remove(worldTroop);
			RedisMapperUtil.delete(worldTroop);
		}
	}

	public void removeWorldTroopForCache(String id) {
		this.playerTroopMap.remove(id);
	}
	
	public Map<String, WorldTroop> getPlayerTroopMap() {
		return playerTroopMap;
	}

	public List<WorldTroop> getWorldTroopByPlayer(BasePlayer player) {
		List<String> troopIds = player.playerTroops().getTroopIdList();
		List<WorldTroop> troopList =  getWorldTroops(troopIds);
		if(troopIds.size() > troopList.size()) {
			for (int i = troopIds.size()-1; i >= 0; i--) {
				if(!this.playerTroopMap.containsKey(troopIds.get(i))) {
					troopIds.remove(i);
				}
			}
			player.playerTroops().SetChanged();
			player.saveDB();
		}
		return troopList;
	}
	
	public List<WorldTroop> getWorldTroopByPlayers(List<Long> playerIds) {
		return playerTroopMap.values().stream().filter(e -> playerIds.contains(e.getPlayerId())).collect(Collectors.toList());
	}
	
	public List<WorldTroop> getWorldTroops(List<String> troopIds) {
		return troopIds.stream()
				.map(e -> getWorldTroop(e))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	public List<Integer> getPlayerTroopTankList(Player player) {
		List<String> troopIds = player.playerTroops().getTroopIdList();
		return troopIds.stream()
				.map(e -> getWorldTroop(e))
				.filter(Objects::nonNull)
				.flatMap(e -> e.getTroopArmy().getTankList().stream().map(c -> c.getId()))
				.collect(Collectors.toList());
	}

}
