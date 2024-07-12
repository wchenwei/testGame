package com.hm.action.cityworld.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;

@Biz
public class WorldDeclareWarBiz{
	/**
	 * 是否是宣战军团
	 * @param player
	 * @param worldCity
	 * @return
	 */
	public boolean isEnemyGuild(Player player,WorldCity worldCity) {
		return isEnemyGuild(player, worldCity.getBelongGuildId());
	}
	
	public boolean isEnemyGuild(Player player,int GuildId) {
		return isEnemyGuild(player.getServerId(), player.getGuildId(), GuildId);
	}

	public boolean isEnemyGuild(int serverId,int myGuildId,int GuildId) {
		if(GuildId == myGuildId) {
			return false;
		}
		return true;
	}



}
