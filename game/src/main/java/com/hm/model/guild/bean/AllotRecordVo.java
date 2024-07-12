package com.hm.model.guild.bean;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllotRecordVo{
	private long playerId;
	private long targetPlayerId;
	private int id;
	private long time;
	private String name;
	private String targetName;
	
	public AllotRecordVo(AllotRecord record){
		this.id = record.getId();
		this.targetPlayerId = record.getTargetPlayerId();
		this.playerId = record.getPlayerId();
		this.time = record.getTime();
		PlayerRedisData playerData = RedisUtil.getPlayerRedisData(playerId);
		PlayerRedisData targetData = RedisUtil.getPlayerRedisData(this.getTargetPlayerId());
		this.name = playerData==null?this.playerId+"":playerData.getName();
		this.targetName = targetData == null?this.getTargetPlayerId()+"":targetData.getName();
	}

}
