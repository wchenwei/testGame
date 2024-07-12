package com.hm.model.serverpublic.kf.pk;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KfPkKingInfo {
	private int id;
	public int serverId;
	public String name;
	public String icon;
	public int frameIcon;
	
	public KfPkKingInfo(int id) {
		super();
		this.id = id;
		loadPlayerInfo();
	}

	public void loadPlayerInfo() {
		this.serverId = ServerUtils.getCreateServerId(id);
		PlayerRedisData redisData = RedisUtil.getPlayerRedisData(id);
		if(redisData != null) {
			this.icon = redisData.getIcon(); 
			this.name = redisData.getName();
			this.frameIcon = redisData.getFrameIcon();
		}
		
	}
}
