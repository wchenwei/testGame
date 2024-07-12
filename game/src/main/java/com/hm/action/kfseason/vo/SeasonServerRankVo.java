package com.hm.action.kfseason.vo;

import com.hm.model.activity.kfseason.server.KFSeasonServer;
import com.hm.redis.ServerNameCache;

public class SeasonServerRankVo {
	private int id;//服务器id
	private int score;//赛季积分
	private String name;//服务器名字
	
	public SeasonServerRankVo(KFSeasonServer seasonServer) {
		this.id = seasonServer.getId();
		this.score = seasonServer.getScore();
        this.name = ServerNameCache.getServerName(this.id);
	}
}
