package com.hm.action.guild.command;

import com.hm.action.guild.AbstractGuildAction;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.command.AbstractGuildCommand;
import com.hm.model.player.Player;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;


@Action
public class GuildCommandAction extends AbstractGuildAction {

	@Resource
	private GuildCommandBiz guildCommandBiz;

	
	@MsgMethod(MessageComm.S2C_GuildCommand_Create)
    public void createCommand(Player player, Guild guild, JsonMsg msg) {
		//只有部落长副部落长能升级
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		int index = msg.getInt("index");
		guild.getGuildCommands().removeGuildCommand(index);
		AbstractGuildCommand command = guildCommandBiz.checkAndCreate(player,guild, msg);
		if(command == null) {
			return;
		}
		guild.getGuildCommands().addGuildCommand(index, command);
		//更新
		guildCommandBiz.broadGuildCommandUpdate(guild);
		//广播更新
		guildCommandBiz.sendChat(player, command);
	}
	
	@MsgMethod(MessageComm.S2C_GuildCommand_Del)
    public void delCommand(Player player, Guild guild, JsonMsg msg) {
		//只有部落长副部落长能升级
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		int index = msg.getInt("index");
		guild.getGuildCommands().removeGuildCommand(index);
		//更新
		guildCommandBiz.broadGuildCommandUpdate(guild);

	}

	

}
















