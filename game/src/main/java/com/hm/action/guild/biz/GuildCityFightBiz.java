package com.hm.action.guild.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.action.cityworld.biz.WorldDeclareWarBiz;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerAllianceData;
import com.hm.model.serverpublic.ServerDataManager;

import javax.annotation.Resource;

@Biz
public class GuildCityFightBiz {
	@Resource
	private WorldDeclareWarBiz worldDeclareWarBiz;
	
	/**
	 * 是友方城镇
	 * @param player
	 * @param worldCity
	 * @return
	 */
	public boolean isFriendCity(Player player,WorldCity worldCity) {
		return isFriendGuild(player, worldCity.getBelongGuildId());
	}
	public boolean isFriendGuild(Player player,int oppoGuild) {
		return isFriendGuild(player.getServerId(), player.getGuildId(), oppoGuild);
	}
	public boolean isFriendGuild(int serverId,int guildId,int oppoGuild) {
		if(guildId == oppoGuild) {
			return true;
		}
		return false;
//		//判断是否结盟
//		ServerAllianceData serverAllianceData = ServerDataManager.getIntance().getServerData(serverId).getServerAllianceData();
//		return serverAllianceData.isAllianGuild(guildId, oppoGuild);
	}
	/**
	 * 是我方城镇
	 * @param player
	 * @param worldCity
	 * @return
	 */
	public boolean isSameGuild(Player player,WorldCity worldCity) {
		return player.getGuildId() == worldCity.getBelongGuildId();
	}
	
	/**
	 * 是同一阵营
	 * @param GuildId
	 * @param worldCity
	 * @return
	 */
	public boolean isSameGuild(int GuildId,WorldCity worldCity) {
		return GuildId == worldCity.getBelongGuildId();
	}
	
	/**
	 * 是敌方城镇
	 * @param player
	 * @param worldCity
	 * @return
	 */
	public boolean isEnemyCity(Player player,WorldCity worldCity) {
		return player.getGuildId() != worldCity.getBelongGuildId();
	}
}
