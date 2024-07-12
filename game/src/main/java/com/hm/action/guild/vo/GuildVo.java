
package com.hm.action.guild.vo;

import com.hm.model.guild.Guild;
import lombok.Data;

import java.util.Date;

@Data
public class GuildVo {
	private int guildId;
	private String guildName;
	private String flagName;//军团旗帜的字
	private int count;
	private int lv;
	private long combat;
	private Date createTime;
	private long lastLoginTime;// 所有成员最后一次登录时间
	private long lastLeaderLoginTime;// 首领最后一次登录时间
	private long tecResetTime;
	
	public GuildVo(Guild guild) {
		this.guildId = guild.getId();
		this.guildName = guild.getGuildInfo().getGuildName();
		this.count = guild.getGuildMembers().getNum();
		this.lv = guild.guildLevelInfo().getLv();
		this.combat = guild.getGuildMembers().getTotalCombat();
		this.createTime = guild.getGuildInfo().getCreateTime();
		this.flagName = guild.getGuildFlag().getFlagName();
		this.tecResetTime= guild.guildTechnology().getResetMsec();
		this.lastLoginTime = guild.getGuildInfo().getLastLoginTime();
		this.lastLeaderLoginTime = guild.getGuildInfo().getLastLeaderLoginTime();
	}

	public static GuildVo buildVo(Guild guild){
		return new GuildVo(guild);
	}
}

