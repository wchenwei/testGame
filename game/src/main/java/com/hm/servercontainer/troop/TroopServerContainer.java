package com.hm.servercontainer.troop;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class TroopServerContainer{
	@Getter
	private static ContainerMap<TroopItemContainer> serverMap =
			new ContainerMap<>(serverId -> new TroopItemContainer(serverId));

	public static TroopItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static TroopItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}


	public static TroopItemContainer of(Player player) {
		if(player.isKFPlayer()) {
			return serverMap.getItemContainer(player.playerTemp().getKfpServerUrl().getServerId());
		}
		return serverMap.getItemContainer(player.getServerId());
	}

	public static TroopItemContainer of(WorldTroop worldTroop) {
		int kfServerId = worldTroop.getKFServerId();
		if(kfServerId > 0) {
			return serverMap.getItemContainer(kfServerId);
		}
		return serverMap.getItemContainer(worldTroop.getServerId());
	}
}
