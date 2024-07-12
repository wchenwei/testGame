package com.hm.action.player.vo;

import com.google.common.collect.Lists;
import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.redis.ServerNameCache;
import lombok.Data;

import java.util.List;

@Data
public class PlayerDetailVo extends SimplePlayerVo {
	private int serverId;
	private String serverName;
	private List<TroopDetailVo> troopList = Lists.newArrayList();

	public PlayerDetailVo(Player player) {
		this.load(player);
		this.serverId = player.getCreateServerId();
		this.serverName = ServerNameCache.getServerNameByPlayerId(player.getId());
	}

	public <T extends WorldTroop> void loadTroopList(Player player,List<T> troopList) {
		for (int i = 0; i < troopList.size(); i++) {
			TroopDetailVo troopDetailVo = new TroopDetailVo();
			troopDetailVo.setId(i+1);
			troopDetailVo.loadTroop(player,troopList.get(i));

			this.troopList.add(troopDetailVo);
		}
	}
}
