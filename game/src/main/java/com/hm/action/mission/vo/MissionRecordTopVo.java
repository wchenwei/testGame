package com.hm.action.mission.vo;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.mission.MissionTopRecord;

public class MissionRecordTopVo{
	public String name;
	public String icon;
	public int frameIcon;
	private long combat;
	private long playerId;
	private String tanks;
	private int superLv;
	
	public MissionRecordTopVo(MissionTopRecord record) {
		PlayerRedisData redisData = RedisUtil.getPlayerRedisData(record.getPlayerId());
		this.playerId = record.getPlayerId();
		this.icon = redisData.getIcon(); 
		this.name = redisData.getName();
		this.frameIcon = redisData.getFrameIcon();
		this.combat = record.getCombat();
		this.tanks = record.getTanks();
		this.superLv = record.getSuperLv();
	}
}
