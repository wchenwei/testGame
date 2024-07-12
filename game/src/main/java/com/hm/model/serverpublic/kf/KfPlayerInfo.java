package com.hm.model.serverpublic.kf;

import com.hm.redis.PlayerRedisData;

public class KfPlayerInfo {
	private long playerId;
	public String name;
	public String icon;
	public int frameIcon;
	public long combat;
	
	public void load(PlayerRedisData redisData) {
		if (redisData == null) {
			return;
		}
		this.playerId = redisData.getId();
		this.icon = redisData.getIcon(); 
		this.name = redisData.getName();
		this.frameIcon = redisData.getFrameIcon();
		this.combat = redisData.getCombat();
	}
}
