package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.model.guild.Guild;
import com.hm.model.tank.TankVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlayerTroopVo extends SimplePlayerVo{
	public PlayerTroopVo(Player tarPlayer, Guild guild,Map<Integer,List<TankVo>> troops) {
		this.load(tarPlayer);
		this.vip = tarPlayer.getPlayerVipInfo().getVipLv();
		this.troops = troops;
		if(null!=guild) {
			this.flagName = guild.getGuildFlag().getFlagName();
			this.guildName= guild.getGuildInfo().getGuildName();
			this.guildJob = guild.getGuildMembers().getJob(tarPlayer.getId()).getType();
		}
	}
	private int guildFlag;
	private String flagName;
	private int vip;
	private String guildName;//部落名字
	private int dw;	//段位
	private int guildJob;//部落职位
	private int rank;//排行榜
	private long pvpRank;//竞技场排行榜
	private long combatRank;//最强战力排行
	private Map<Integer,List<TankVo>> troops = Maps.newConcurrentMap();
	//private Map<Integer,List<TankArmy>> troops = Maps.newConcurrentMap();
}
