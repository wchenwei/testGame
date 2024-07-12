package com.hm.action.cityworld.vo;

import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FightTroopVo extends SimplePlayerVo{
	public String id;
	public int state;
	public int index;
	public boolean isNpc;
	public int serverId;
	
	public FightTroopVo(WorldTroop worldTroop) {
		this.id = worldTroop.getId();
		this.state = worldTroop.getState();
		this.index = worldTroop.getTroopInfo().getIndex();
		this.serverId = worldTroop.getServerId();

		Player player = PlayerUtils.getPlayerFromOnlineOrCache(worldTroop.getPlayerId());
		if(player == null) {
			this.load(RedisUtil.getPlayerRedisData(worldTroop.getPlayerId()));
		}else{
			this.load(player);
		}
		this.loadGuild(worldTroop);
	}
	
}
