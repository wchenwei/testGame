package com.hm.leaderboards;

import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;

public class GuildRankData {
	private int id;
	private String name;
	private String flag;
	private int flagId;
	private int job;
	
	public GuildRankData(Guild guild,long playerId) {
		this.id = guild.getId();
		this.name = guild.getGuildInfo().getGuildName();
		this.flag = guild.getGuildFlag().getFlagName();
		GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(playerId);
		if(guildPlayer != null) {
			this.job = guildPlayer.getGuildJob();
		}
	}
}
