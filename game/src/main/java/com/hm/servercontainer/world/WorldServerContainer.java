package com.hm.servercontainer.world;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class WorldServerContainer{
	@Getter
	private static ContainerMap<WorldItemContainer> serverMap =
			new ContainerMap<>(serverId -> new WorldItemContainer(serverId));

	public static WorldItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static WorldItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}

	public static WorldItemContainer of(Player player) {
		if(player.isKFPlayer()) {
			return serverMap.getItemContainer(player.playerTemp().getKfpServerUrl().getServerId());
		}
		return serverMap.getItemContainer(player.getServerId());
	}

	public static WorldItemContainer of(WorldTroop worldTroop) {
		int kfServerId = worldTroop.getKFServerId();
		if(kfServerId > 0) {
			return serverMap.getItemContainer(kfServerId);
		}
		return serverMap.getItemContainer(worldTroop.getServerId());
	}

}
