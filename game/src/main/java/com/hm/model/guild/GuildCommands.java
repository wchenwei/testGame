package com.hm.model.guild;

import com.hm.model.guild.command.AbstractGuildCommand;

public class GuildCommands {
	private AbstractGuildCommand[] commands = new AbstractGuildCommand[8];
	
	
	public void addGuildCommand(int index,AbstractGuildCommand command) {
		this.commands[index] = command;
	}
	
	public void removeGuildCommand(int index) {
		this.commands[index] = null;
	}
	
	public AbstractGuildCommand[] getCommands() {
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] != null && commands[i].isOver()) {
				this.commands[i] = null;
			}
		}
		return commands;
	}
	
	public AbstractGuildCommand getGuildCommand(int index) {
		AbstractGuildCommand command = this.commands[index];
		if(command != null && command.isOver()) {
			this.commands[index] = null;
			return null;
		}
		return command;
	}
	
	public boolean checkGuildCommandCity(int cityId) {
		for (int i = 0; i < commands.length; i++) {
			if(commands[i] != null && commands[i].isFitCityId(cityId)) {
				this.commands[i] = null;
				return true;
			}
		}
		return false;
	}
	

	public static void main(String[] args) {
		AbstractGuildCommand[] commandList = new AbstractGuildCommand[4];
	}
}
