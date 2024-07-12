package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.hm.model.guild.bean.GuildLog;

import java.util.ArrayList;

public class GuildLogs extends GuildComponent{
	private ArrayList<GuildLog> guildLog = Lists.newArrayList();
	
	public void addLog(GuildLog agLog) {
		this.guildLog.add(agLog);
		
	}
}
