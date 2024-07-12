package com.hm.model.serverpublic;

import com.google.common.collect.Maps;

import java.util.Map;

public class ServerDataManager {
	private static ServerDataManager intance = new ServerDataManager();
	public static ServerDataManager getIntance() {
		return intance;
	}
	private Map<Integer,ServerData> dataMap = Maps.newConcurrentMap();
	
	public void loadServerData(int serverId) {
		ServerData serverData = ServerDataDbUtils.getServerData(serverId);
		if(serverData == null) {
			serverData = new ServerData(serverId);
			serverData.getServerOpenData().initOpenDate();
			serverData.getServerStatistics().loadFirstRechargrTankId();
			serverData.save();
		}
		//加载服务器状态数据 -白名单 //TODO 测试
//		serverData.getServerStatusData().loadServerStatus(serverId);
		dataMap.put(serverId, serverData);
		serverData.loadServerData();
	}
	
	//是否是第一次启动服务器
	public boolean isFirstStartServer(int serverId) {
		return ServerDataDbUtils.getServerData(serverId) == null;
	}
	
	public ServerData getServerData(int serverId) {
		return dataMap.get(serverId);
	}
	
	public void removeServerData(int serverId) {
		this.dataMap.remove(serverId);
	}
	
	public void checkAllFirstBaseNpclv() {
		for (ServerData serverData : dataMap.values()) {
			ServerDataUtils.checkFirstBaseLv(serverData);
		}
	}
}	
