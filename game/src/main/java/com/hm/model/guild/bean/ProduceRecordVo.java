package com.hm.model.guild.bean;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduceRecordVo{
	private long playerId;
	private long time;
	private String name;
	
	public ProduceRecordVo(ProduceRecord record){
		this.playerId = record.getPlayerId();
		this.time = record.getTime();
		PlayerRedisData playerData = RedisUtil.getPlayerRedisData(this.getPlayerId());
		this.name = playerData==null?this.getPlayerId()+"":playerData.getName();
	}

}
