package com.hm.servercontainer.guild;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class GuildContainer{
	@Getter
	private static ContainerMap<GuildItemContainer> serverMap =
			new ContainerMap<>(serverId -> new GuildItemContainer(serverId));

	public static GuildItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static GuildItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}
}
