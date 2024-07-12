package com.hm.servercontainer.centreArms;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class CentreArmsContainer{

	@Getter
	private static ContainerMap<CentreArmsItemContainer> serverMap =
			new ContainerMap<>(serverId -> new CentreArmsItemContainer(serverId));

	public static CentreArmsItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static CentreArmsItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}
}
