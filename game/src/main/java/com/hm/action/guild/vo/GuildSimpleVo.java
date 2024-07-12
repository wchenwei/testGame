package com.hm.action.guild.vo;

import com.hm.model.guild.Guild;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuildSimpleVo {
	private int id;
	private String name;
	private String flag;
	private int flagId;
	
	public GuildSimpleVo(Guild guild) {
		this.id = guild.getId();
		this.name = guild.getGuildInfo().getGuildName();
		this.flag = guild.getGuildFlag().getFlagName();
	}

}
