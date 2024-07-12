package com.hm.servercontainer.mail;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.servercontainer.ContainerMap;
import lombok.Getter;

public class MailServerContainer {
	@Getter
	private static ContainerMap<MailItemContainer> serverMap =
			new ContainerMap<>(serverId -> new MailItemContainer(serverId));

	public static MailItemContainer of(int serverId) {
		return serverMap.getItemContainer(serverId);
	}

	public static MailItemContainer of(DBEntity entity) {
		return serverMap.getItemContainer(entity);
	}
}







