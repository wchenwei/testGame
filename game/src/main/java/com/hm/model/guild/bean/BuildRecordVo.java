package com.hm.model.guild.bean;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildRecordVo{
	private long playerId;
	private int type;
	private long time;
	private String name;
	private int count;

	public BuildRecordVo(BuildRecord buildRecord) {
		this.playerId = buildRecord.getPlayerId();
		PlayerRedisData playerData = RedisUtil.getPlayerRedisData(buildRecord.getPlayerId());
		this.name = playerData==null?this.getPlayerId()+"":playerData.getName();
		this.type = buildRecord.getType();
		this.time = buildRecord.getTime();
		this.count = buildRecord.getCount();
	}

}
