package com.hm.servercontainer.supplyTroop;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.servercontainer.ItemContainer;
import com.hm.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SupplyTroopItemContainer extends ItemContainer{
	//玩家部队map key:部队id  value:部队
	private Map<String,SupplyTroop> playerTroopMap = Maps.newConcurrentMap();
	
	public SupplyTroopItemContainer(int serverId) {
		super(serverId);
	}
	
	@Override
	public void initContainer() {
		for (SupplyTroop worldTroop : RedisMapperUtil.queryListAll(getServerId(),SupplyTroop.class)) {
			this.playerTroopMap.put(worldTroop.getId(), worldTroop);
		}
	}
	
	public void addSupplyTroop(SupplyTroop supplyTroop) {
		this.playerTroopMap.put(supplyTroop.getId(), supplyTroop);
		supplyTroop.saveDB();
	}
	
	public SupplyTroop getSupplyTroop(String id) {
		if(StrUtil.isEmpty(id)) {
			return null;
		}
		return this.playerTroopMap.get(id);
	}
	public void removeSupplyTroop(String id) {
		SupplyTroop supplyTroop = this.playerTroopMap.remove(id);
		if(supplyTroop != null) {
//			getMongodDB().remove(supplyTroop);
			supplyTroop.delete();
		}
	}
	
	//随机敌方部队
	public List<SupplyTroop> randomSupplyTroop(Player player,int count) {
		long playerId = player.getId();
		List<SupplyTroop> allList = this.playerTroopMap.values().stream()
				.filter(e -> !e.isRob()).filter(e -> e.getPlayerId() != playerId)
				.collect(Collectors.toList());
		return RandomUtils.randomEleList(allList, count);
	}
	
	public Map<String, SupplyTroop> getSupplyTroop() {
		return playerTroopMap;
	}

	public List<SupplyTroop> getSupplyTroopByPlayer(BasePlayer player) {
		List<String> troopIds = player.playerRobSupply().getTroopIdList();
		List<SupplyTroop> troopList =  getSupplyTroops(troopIds);
		if(troopIds.size() > troopList.size()) {
			for (int i = troopIds.size()-1; i >= 0; i--) {
				if(!this.playerTroopMap.containsKey(troopIds.get(i))) {
					troopIds.remove(i);
				}
			}
			player.playerRobSupply().SetChanged();
		}
		return troopList;
	}
	
	public List<SupplyTroop> getSupplyTroops(List<String> troopIds) {
		return troopIds.stream()
				.map(e -> getSupplyTroop(e))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
