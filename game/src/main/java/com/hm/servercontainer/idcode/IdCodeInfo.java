package com.hm.servercontainer.idcode;

import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@RedisMapperType(type = MapperType.HASH_JSON)
public class IdCodeInfo extends BaseEntityMapper<String> {
	private ConcurrentHashMap<Long,Integer> playerMap = new ConcurrentHashMap();
	public IdCodeInfo() {
		super();
	}
	public IdCodeInfo(int serverId,int createServerId,String idCode) {
		this.setServerId(serverId);
		this.setId(idCode+"_"+createServerId);
	}
	public void addPlayerId(long playerId) {
		int state = ServerConfig.getInstance().isIdCodeSwitch()?1:0;
		playerMap.put(playerId, state);
	}
	public void delId(long id) {
		this.playerMap.remove(id);
	}
	public int getBindNum() {
		return (int) playerMap.values().stream().filter(t ->t==1).count();
	}

	public ConcurrentHashMap<Long, Integer> getPlayerMap() {
		return playerMap;
	}
	public int getState(long id) {
		return playerMap.getOrDefault(id, 0);
	}
	public void del(List<Long> playerIds) {
		playerIds.forEach(t ->playerMap.remove(t));
	}
	

}
