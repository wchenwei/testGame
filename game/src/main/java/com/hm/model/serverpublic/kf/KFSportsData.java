package com.hm.model.serverpublic.kf;

import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KFSportsData extends AbstractKfData{
	private long playerId;
	private int serverId;
	private KfPlayerInfo playerInfo;
	
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		this.serverId = ServerUtils.getCreateServerId(playerId);
		loadPlayerInfo();
	}
	
	public void loadPlayerInfo() {
		this.serverId = ServerUtils.getCreateServerId(playerId);
		this.playerInfo = new KfPlayerInfo();
		this.playerInfo.load(RedisUtil.getPlayerRedisData(playerId));
	}
	
	public boolean loadHourCheck() {
		if(!haveWinPlayer()) {
			return false;
		}
		loadPlayerInfo();
		return true;
	}
	
	public boolean haveWinPlayer() {
		return this.playerId > 0;
	}
}
