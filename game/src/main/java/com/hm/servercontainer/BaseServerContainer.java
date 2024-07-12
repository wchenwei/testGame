package com.hm.servercontainer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.server.GameServerManager;

import java.util.List;
import java.util.Map;

/**
 * 总服务器容器
 * @author xiaoaogame
 *
 * @param <T>
 */
public abstract class BaseServerContainer<T extends ItemContainer> {
	private Map<Integer,T> dataMap = Maps.newConcurrentMap();
	
	//加载所有服务器的container数据
	public void loadDataMap() {
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			putAndNewServerContainer(serverId);
		});
	}
	public void putAndNewServerContainer(int serverId) {
		T container = loadOneServerContainer(serverId);
		container.initContainer();
		putItem(serverId, container);
	}
	
	//获取单服务器的container
	public abstract T loadOneServerContainer(int serverId);
	
	public T getItemContainer(int serverId) {
		return dataMap.get(serverId);
	}
	
	public T getItemContainer(DBEntity entity) {
		return getItemContainer(entity.getServerId());
	}
	
	public void putItem(int serverId,T t) {
		this.dataMap.put(serverId, t);
	}
	
	public void removeItem(int serverId) {
		this.dataMap.remove(serverId);
	}
	
	public List<T> getAllContainer() {
		return Lists.newArrayList(this.dataMap.values());
	}
	
	public void clearItem() {
		this.dataMap.clear();
	}
}
