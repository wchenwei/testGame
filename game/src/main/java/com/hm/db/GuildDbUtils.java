package com.hm.db;

import com.hm.model.guild.Guild;

//部落的数据库工具类
public class GuildDbUtils extends CommonDbUtil{
	public static void saveGuild(Guild guild) {
		insert(guild);
	}
	public static Guild getGuild(int id, int serverId) {
		return getMongoDB(serverId).get(id, Guild.class);
	}
}
