package com.hm.action.guild.vo;

import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import com.hm.model.worldtroop.WorldTroop;

public class GuildTroopVo extends SimplePlayerVo {
	private TroopVo troop;
	public GuildTroopVo(Player player,WorldTroop worldTroop) {
		this.load(player);
		troop = new TroopVo(player, worldTroop);
	}
	public TroopVo getTroop() {
		return troop;
	}

}
