package com.hm.redis.data;

import com.hm.model.guild.Guild;
import lombok.Data;

@Data
public class GuildRedisData {
	public GuildRedisData() {}
	public GuildRedisData(Guild guild) {
		this.id = guild.getId();
		this.guildName = guild.getGuildInfo().getGuildName();
		this.flagName = guild.getGuildFlag().getFlagName();
		this.serverId = guild.getServerId();
		this.lv = guild.guildLevelInfo().getLv();
		this.lastLonginTime = guild.getGuildInfo().getLastLoginTime();
	}
	private int serverId;
	private int lv;
	private long lastLonginTime;
	private int id;
	private String guildName;	//军团名字
	private String flagName;//军团旗帜的字
}
