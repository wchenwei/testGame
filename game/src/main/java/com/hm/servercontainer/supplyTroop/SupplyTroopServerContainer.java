package com.hm.servercontainer.supplyTroop;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class SupplyTroopServerContainer{
	@Getter
	private static ContainerMap<SupplyTroopItemContainer> serverMap =
			new ContainerMap<>(serverId -> new SupplyTroopItemContainer(serverId));

	public static SupplyTroopItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static SupplyTroopItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}

}
