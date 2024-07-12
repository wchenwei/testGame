package com.hm.action.task.biz;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.task.GuildTaskBiz;
import com.hm.action.overallWar.biz.OverallWarBiz;
import com.hm.enums.PlayerFunctionType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.guild.Guild;
import com.hm.model.player.BasePlayer;

import javax.annotation.Resource;

@Biz
public class TaskOpenBiz {
	@Resource
	private OverallWarBiz overallWarBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildTaskBiz guildTaskBiz;

	//叛军要塞是否开启
	public boolean todayServerBossIsOpen(BasePlayer player) {
		return player.getPlayerFunction().isOpenFunction(PlayerFunctionType.WorldBoss);
	}
	
	//全面战争是否开启
	public boolean todayOverallWarIsOpen(BasePlayer player) {
		return overallWarBiz.isOverallWarOpenDay(player.getServerId(), DateUtil.getCsWeek());
	}
	
	//部落任务是否开启
	public boolean playerGuildTaskIsOpen(BasePlayer player) {
		Guild guild = guildBiz.getGuild(player);
		if(guild == null || !guildTaskBiz.isGuildCanTask(guild)) {
			return false;
		}
		return true;
	}
	
	
	
	
	
}
