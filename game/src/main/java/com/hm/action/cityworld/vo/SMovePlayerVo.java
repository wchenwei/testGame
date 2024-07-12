package com.hm.action.cityworld.vo;

import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SMovePlayerVo{
	public long id;
	public int gid;
	public String name;
	public int carLv;//车长等级
	public int[] equQuality;//装备品质

	public SMovePlayerVo(long playerId) {
		this.id = playerId;

		Player player = PlayerUtils.getPlayerFromOnlineOrCache(playerId);
		if(player != null) {
			this.carLv = player.playerCommander().getCarLv();
			this.equQuality = player.playerEquip().getEquQuality();
			this.name = player.getName();
			this.gid = player.getGuildId();
		}else{
			PlayerRedisData redisData = RedisUtil.getPlayerRedisData(playerId);
			this.carLv = redisData.getCarLv();
			this.equQuality = redisData.getEquQuality();
			this.name = redisData.getName();
			this.gid = redisData.getGuildId();
		}
	}

}
