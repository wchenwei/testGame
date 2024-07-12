package com.hm.servercontainer.idcode;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class IdCodeContainer{

	@Getter
	private static ContainerMap<IdCodeItemContainer> serverMap =
			new ContainerMap<>(serverId -> new IdCodeItemContainer(serverId));

	public static IdCodeItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static IdCodeItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}
}
