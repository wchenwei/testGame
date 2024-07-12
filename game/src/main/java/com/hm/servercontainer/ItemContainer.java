package com.hm.servercontainer;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;

/**
 * 单服务器的容器
 * @author xiaoaogame
 *
 */
public abstract class ItemContainer {
	private int serverId;
	
	public ItemContainer(int serverId) {
		this.serverId = serverId;
	}
	public abstract void initContainer();
	
	public int getServerId() {
		return serverId;
	}
	
	public MongodDB getMongodDB() {
		return MongoUtils.getMongodDB(serverId);
	}
}
