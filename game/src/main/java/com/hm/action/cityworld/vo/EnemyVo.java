package com.hm.action.cityworld.vo;

import com.hm.model.guild.Guild;

public class EnemyVo {
	private long id;
	private long endTime;
	private String name;
	private String flag;
	private int flagId;
	
	public EnemyVo(int guildId,long endTime) {
		this.id = guildId;
		this.endTime = endTime;
	}
	
	public void loadGuildData(Guild guild) {
		this.name = guild.getGuildInfo().getGuildName();
		this.flag = guild.getGuildFlag().getFlagName();
	}
}
