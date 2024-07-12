package com.hm.servercontainer.activity;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class ActivityServerContainer{
	@Getter
	private static ContainerMap<ActivityItemContainer> serverMap =
			new ContainerMap<>(serverId -> new ActivityItemContainer(serverId));

	public static ActivityItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static ActivityItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}

}







