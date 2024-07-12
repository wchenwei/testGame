package com.hm.servercontainer.worldbuild;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class WorldBuildTroopServerContainer{
	@Getter
	private static ContainerMap<WorldBuildTroopItemContainer> serverMap =
			new ContainerMap<>(serverId -> new WorldBuildTroopItemContainer(serverId));

	public static WorldBuildTroopItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static WorldBuildTroopItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}
	
	public static void saveAll() {
		try {
			for (WorldBuildTroopItemContainer worldBuildTroopItemContainer : WorldBuildTroopServerContainer.getServerMap().getAllContainer()) {
				for (PlayerWorldBuild playerWorldBuild : worldBuildTroopItemContainer.getPlayerMap().values()) {
					playerWorldBuild.saveDB();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveAll(int serverId) {
		try {
			WorldBuildTroopItemContainer worldBuildTroopItemContainer = WorldBuildTroopServerContainer.of(serverId);
			if(worldBuildTroopItemContainer != null) {
				for (PlayerWorldBuild playerWorldBuild : worldBuildTroopItemContainer.getPlayerMap().values()) {
					playerWorldBuild.saveDB();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
